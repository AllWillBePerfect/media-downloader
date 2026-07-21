package com.awbp.repository;

import com.awbp.mapper.DownloadJobMapper;
import com.awbp.domain.DownloadJob;
import com.awbp.ports.DownloadJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DownloadJobRepositoryAdapter implements DownloadJobRepository {

    private final DownloadJobJpaRepository repository;

    @Override
    public DownloadJob save(DownloadJob job) {
        return DownloadJobMapper.toDomain(repository.save(DownloadJobMapper.toEntity(job)));
    }

    @Override
    public Optional<DownloadJob> findById(UUID id) {
        return repository.findById(id).map(DownloadJobMapper::toDomain);
    }

    @Override
    public Optional<DownloadJob> findByUserId(Long id) {
        return repository.findByUserId(id).map(DownloadJobMapper::toDomain);
    }
}
