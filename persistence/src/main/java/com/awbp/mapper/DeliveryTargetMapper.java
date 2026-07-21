package com.awbp.mapper;

import com.awbp.domain.DeliveryTarget;
import com.awbp.entity.DeliveryTargetEntity;

public final class DeliveryTargetMapper {

    private DeliveryTargetMapper() {}

    public static DeliveryTarget toDomain(DeliveryTargetEntity entity) {
        return new DeliveryTarget(
                entity.getId(),
                entity.getJobId(),
                entity.getDeliveryType(),
                entity.getTarget(),
                entity.getCreatedAt()
        );
    }

    public static DeliveryTargetEntity toEntity(DeliveryTarget domain) {
        return DeliveryTargetEntity.builder()
                .id(domain.id())
                .jobId(domain.jobId())
                .deliveryType(domain.deliveryType())
                .target(domain.target())
                .createdAt(domain.createdAt())
                .build();
    }
}
