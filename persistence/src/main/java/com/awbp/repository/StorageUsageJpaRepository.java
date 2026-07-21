package com.awbp.repository;

import com.awbp.entity.StorageUsageEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StorageUsageJpaRepository
        extends JpaRepository<StorageUsageEntity, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from StorageUsageEntity s where s.id = 1")
    Optional<StorageUsageEntity> findGlobalForUpdate();
}