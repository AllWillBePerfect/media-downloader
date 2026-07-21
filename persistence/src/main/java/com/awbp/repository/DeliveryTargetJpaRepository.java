package com.awbp.repository;

import com.awbp.entity.DeliveryTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryTargetJpaRepository extends JpaRepository<DeliveryTargetEntity, UUID> {

    public Optional<DeliveryTargetEntity> findByJobId(UUID jobId);
}
