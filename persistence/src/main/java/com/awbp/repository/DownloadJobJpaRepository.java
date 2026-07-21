package com.awbp.repository;

import com.awbp.entity.DownloadJobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface DownloadJobJpaRepository extends JpaRepository<DownloadJobEntity, UUID> {

    Optional<DownloadJobEntity> findByUserId(Long uuid);
}