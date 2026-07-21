package com.awbp.mapper;

import com.awbp.rest.dto.DownloadResponse;
import com.awbp.domain.DownloadJob;

public class DownloadMapper {
    private DownloadMapper() {
    }

    public static DownloadResponse toResponse(DownloadJob job) {
        return new DownloadResponse(
                job.id(),
                job.userId(),
                job.url(),
                job.type(),
                job.status(),
                job.createdAt(),
                job.updatedAt()
        );
    }
}
