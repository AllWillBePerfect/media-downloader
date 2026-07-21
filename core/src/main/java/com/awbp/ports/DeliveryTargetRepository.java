package com.awbp.ports;

import com.awbp.domain.DeliveryTarget;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryTargetRepository {

    DeliveryTarget save(DeliveryTarget deliveryTarget);
    Optional<DeliveryTarget> getByJobId(UUID jobId);
}
