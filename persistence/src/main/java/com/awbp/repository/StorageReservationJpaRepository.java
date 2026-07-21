package com.awbp.repository;

import com.awbp.entity.StorageReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StorageReservationJpaRepository
        extends JpaRepository<StorageReservationEntity, UUID> {
}
