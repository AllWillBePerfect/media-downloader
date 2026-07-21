package com.awbp.mapper;

import com.awbp.domain.DownloadedFile;
import com.awbp.entity.DownloadedFileEntity;

public final class DownloadedFileMapper {

    private DownloadedFileMapper() {};

    public static DownloadedFile toDomain(DownloadedFileEntity entity) {
        return new DownloadedFile(
                entity.getId(),
                entity.getJobId(),
                entity.getPath(),
                entity.getFilename(),
                entity.getSize(),
                entity.getMimeType(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }

    public static DownloadedFileEntity toEntity(DownloadedFile domain) {
        return DownloadedFileEntity.builder()
                .id(domain.id())
                .jobId(domain.jobId())
                .path(domain.path())
                .filename(domain.fileName())
                .size(domain.size())
                .mimeType(domain.mimeType())
                .expiresAt(domain.expiresAt())
                .createdAt(domain.createdAt())
                .build();
    }
}
