package com.awbp.contact;

import com.awbp.domain.DownloadStatus;

import java.util.UUID;

public record DownloadStatusChangedEvent(
        UUID jobId,
        DownloadStatus status
) {
}
