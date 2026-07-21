package com.awbp.rest.dto;

import com.awbp.domain.DownloadStatus;
import com.awbp.domain.DownloadType;

import java.time.Instant;
import java.util.UUID;

public record DownloadResponse(
        UUID id,
        Long userId,
        String url,
        DownloadType type,
        DownloadStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
