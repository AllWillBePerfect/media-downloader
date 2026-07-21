package com.awbp.ports;

import com.awbp.domain.DownloadJob;

import java.util.Optional;
import java.util.UUID;

public interface DownloadJobRepository {
    DownloadJob save(DownloadJob job);

    Optional<DownloadJob> findById(UUID id);
    Optional<DownloadJob> findByUserId(Long id);
}
