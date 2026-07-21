package com.awbp.domain;

import java.time.Instant;
import java.util.UUID;

public record DownloadedFile(
        UUID id,
        UUID jobId,
        String path,
        String fileName,
        Long size,
        String mimeType,
        Instant expiresAt,
        Instant createdAt
) {

    public DownloadedFile copyWithExpiresAt(Instant expiresAt) {
        return new DownloadedFile(
                id,
                jobId,
                path,
                fileName,
                size,
                mimeType,
                expiresAt,
                createdAt

        );
    }

    public static DownloadedFile create(
            UUID id,
            UUID jobId,
            String path,
            String filename,
            Long size,
            String mimeType
    ) {
        Instant instant = Instant.now();
        return new DownloadedFile(
                id,
                jobId,
                path,
                filename,
                size,
                mimeType,
                null,
                instant
        );
    }
}
