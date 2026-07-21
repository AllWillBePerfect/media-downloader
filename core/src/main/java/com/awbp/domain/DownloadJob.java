package com.awbp.domain;

import java.time.Instant;
import java.util.UUID;

public record DownloadJob(
        UUID id,
        Long userId,
        String url,
        DownloadType type,
//        DeliveryType deliveryType,
        DownloadStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public DownloadJob copyWithStatus(
            DownloadStatus status

    ) {
        return new DownloadJob(
                id,
                userId,
                url,
                type,
//                deliveryType,
                status,
                createdAt,
                updatedAt
        );
    }

    public static DownloadJob create(
            UUID id,
            Long userId,
            String url,
            DownloadType type,
//            DeliveryType deliveryType,
            DownloadStatus status
    ) {
        Instant instant = Instant.now();
        return new DownloadJob(
                id,
                userId,
                url,
                type,
//                deliveryType,
                status,
                instant,
                instant
        );
    }

}
