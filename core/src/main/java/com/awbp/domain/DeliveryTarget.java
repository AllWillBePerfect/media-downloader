package com.awbp.domain;

import java.time.Instant;
import java.util.UUID;

public record DeliveryTarget(
        UUID id,
        UUID jobId,
        DeliveryType deliveryType,
        String target,
        Instant createdAt
) {

    public static DeliveryTarget create(
            UUID id,
            UUID jobId,
            DeliveryType deliveryType,
            String target
    ) {
        Instant instant = Instant.now();
        return new DeliveryTarget(
                id,
                jobId,
                deliveryType,
                target,
                instant
        );
    }
}
