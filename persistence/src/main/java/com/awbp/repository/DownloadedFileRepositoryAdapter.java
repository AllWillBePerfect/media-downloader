package com.awbp.repository;

import com.awbp.domain.DownloadedFile;
import com.awbp.entity.StorageUsageEntity;
import com.awbp.mapper.DownloadedFileMapper;
import com.awbp.ports.DownloadedFileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DownloadedFileRepositoryAdapter implements DownloadedFileRepository {

    private final DownloadedFileJpaRepository repository;
    private final StorageUsageJpaRepository storageUsageRepository;

    @Override
    public DownloadedFile save(DownloadedFile domain) {
        return DownloadedFileMapper.toDomain(repository.save(DownloadedFileMapper.toEntity(domain)));
    }

    @Override
    public Optional<DownloadedFile> getByJobId(UUID jobId) {
        return repository.getByJobId(jobId).map(DownloadedFileMapper::toDomain);
    }

    @Override
    public List<DownloadedFile> findExpired(Instant now) {
        return repository.findByExpiresAtBefore(now).stream()
                .map(DownloadedFileMapper::toDomain)
                .toList();
    }

    @Transactional
    @Override
    public void deleteAndReleaseStorage(UUID fileId) {
        repository.findById(fileId).ifPresent(file -> {
            StorageUsageEntity usage = storageUsageRepository.findGlobalForUpdate()
                    .orElseThrow();

            usage.setUsedBytes(usage.getUsedBytes() - file.getSize());

            repository.delete(file);
        });
    }
}
