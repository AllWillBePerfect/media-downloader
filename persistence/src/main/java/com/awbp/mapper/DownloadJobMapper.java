package com.awbp.mapper;

import com.awbp.domain.DownloadJob;
import com.awbp.entity.DownloadJobEntity;

public final class DownloadJobMapper {

    private DownloadJobMapper() {}

    public static DownloadJob toDomain(DownloadJobEntity entity) {
        return new DownloadJob(
                entity.getId(),
                entity.getUserId(),
                entity.getUrl(),
                entity.getType(),
//                entity.getDeliveryType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static DownloadJobEntity toEntity(DownloadJob domain) {
        return DownloadJobEntity.builder()
                .id(domain.id())
                .userId(domain.userId())
                .url(domain.url())
                .type(domain.type())
//                .deliveryType(domain.deliveryType())
                .status(domain.status())
                .createdAt(domain.createdAt())
                .updatedAt(domain.updatedAt())
                .build();
    }

}
