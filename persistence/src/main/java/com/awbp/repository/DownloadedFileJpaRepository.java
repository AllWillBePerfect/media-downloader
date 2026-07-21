package com.awbp.repository;

import com.awbp.domain.DownloadedFile;
import com.awbp.entity.DownloadedFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DownloadedFileJpaRepository extends JpaRepository<DownloadedFileEntity, UUID> {

    Optional<DownloadedFileEntity> getByJobId(UUID jobId);

    List<DownloadedFileEntity> findByExpiresAtBefore(Instant now);
}
