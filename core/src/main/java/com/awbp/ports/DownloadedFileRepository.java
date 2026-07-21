package com.awbp.ports;

import com.awbp.domain.DownloadedFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DownloadedFileRepository {

    DownloadedFile save(DownloadedFile domain);
    Optional<DownloadedFile> getByJobId(UUID jobId);
    List<DownloadedFile> findExpired(Instant now);

    void deleteAndReleaseStorage(UUID fileId);
}
