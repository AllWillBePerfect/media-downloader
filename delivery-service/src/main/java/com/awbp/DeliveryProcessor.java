package com.awbp;

import com.awbp.contact.DownloadStatusChangedEvent;
import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadStatus;
import com.awbp.ports.DownloadJobRepository;
import com.awbp.ports.DownloadStatusChangedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryProcessor {

    private final DeliveryDispatcher dispatcher;
    private final DownloadJobRepository repository;
    private final DownloadStatusChangedPublisher statusChangedPublisher;


    public void process(
            UUID jobId,
            Path file,
            Long size
    ) {
        DownloadJob job = repository.findById(jobId)
                .orElseThrow();

        changeStatus(job, DownloadStatus.DELIVERING);

        dispatcher.deliver(job, file, size);

        changeStatus(job, DownloadStatus.DELIVERED);

    }

    private void changeStatus(
            DownloadJob job,
            DownloadStatus status
    ) {
        repository.save(job.copyWithStatus(status));

        statusChangedPublisher.publish(
                new DownloadStatusChangedEvent(job.id(), status)
        );
    }
}
