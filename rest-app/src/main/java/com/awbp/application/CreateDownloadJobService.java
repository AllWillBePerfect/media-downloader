package com.awbp.application;

import com.awbp.contact.DownloadRequestedEvent;
import com.awbp.rest.dto.CreateDownloadRequest;
import com.awbp.domain.DeliveryTarget;
import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadStatus;
import com.awbp.ports.DeliveryTargetRepository;
import com.awbp.ports.DownloadJobRepository;
import com.awbp.ports.DownloadRequestedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateDownloadJobService {

    private final DownloadJobRepository downloadJobRepository;
    private final DeliveryTargetRepository deliveryTargetRepository;
    private final DownloadRequestedPublisher downloadRequestedPublisher;

    @Transactional
    public UUID create(CreateDownloadRequest request) {
        UUID jobId = UUID.randomUUID();

        DownloadJob job = DownloadJob.create(
                jobId,
                request.userId(),
                request.url(),
                request.type(),
//                request.deliveryType(),
                DownloadStatus.QUEUED
        );

        DeliveryTarget deliveryTarget = DeliveryTarget.create(
                UUID.randomUUID(),
                jobId,
                request.deliveryType(),
                request.target()

        );

        DownloadJob savedJob = downloadJobRepository.save(job);
        DeliveryTarget saveTarget = deliveryTargetRepository.save(deliveryTarget);

        downloadRequestedPublisher.publish(
                new DownloadRequestedEvent(
                        job.id()
                )
        );

        return savedJob.id();
    }
}
