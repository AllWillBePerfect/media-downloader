package com.awbp;

import com.awbp.contact.DeliveryRequestedEvent;
import com.awbp.contact.DownloadStatusChangedEvent;
import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadStatus;
import com.awbp.domain.DownloadedFile;
import com.awbp.ports.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DownloadProcessor {

    private final DownloadJobRepository jobRepository;
    private final DownloadedFileRepository downloadedFileRepository;
    private final Downloader downloader;
    private final DeliveryRequestedPublisher deliveryRequestedPublisher;
    private final VideoMetadataProvider videoMetadataProvider;
    private final StorageRepository storageRepository;
    private final DownloadStatusChangedPublisher statusChangedPublisher;

    public void process(UUID jobId) {

        DownloadJob job = jobRepository.findById(jobId)
                .orElseThrow();



        try {

            long expectedSize = videoMetadataProvider.getExpectedSize(job);
            boolean reserved = storageRepository.tryReserve(jobId, expectedSize);
            if (!reserved) {
                changeStatus(job, DownloadStatus.REJECTED);
                return;
            }

            changeStatus(job, DownloadStatus.DOWNLOADING);

            DownloadedFile result = downloader.download(job);

            downloadedFileRepository.save(
                    DownloadedFile.create(
                            UUID.randomUUID(),
                            job.id(),
                            result.path(),
                            result.fileName(),
                            result.size(),
                            result.mimeType()
                    )
            );

            storageRepository.commit(jobId, result.size());

            changeStatus(job, DownloadStatus.DOWNLOADED);

            deliveryRequestedPublisher.publish(
                    new DeliveryRequestedEvent(
                            job.id(),
                            result.path(),
                            result.size()
                    )
            );
        } catch (RuntimeException e) {
            storageRepository.release(jobId);

            changeStatus(job, DownloadStatus.FAILED);

            throw e;
        }
    }

    private void changeStatus(
            DownloadJob job,
            DownloadStatus status
    ) {
        jobRepository.save(job.copyWithStatus(status));

        statusChangedPublisher.publish(
                new DownloadStatusChangedEvent(job.id(), status)
        );
    }
}