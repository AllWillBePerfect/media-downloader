package com.awbp.repository;

import com.awbp.domain.DeliveryTarget;
import com.awbp.mapper.DeliveryTargetMapper;
import com.awbp.ports.DeliveryTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryTargetRepositoryAdapter implements DeliveryTargetRepository {

    private final DeliveryTargetJpaRepository repository;

    @Override
    public DeliveryTarget save(DeliveryTarget deliveryTarget) {
        return DeliveryTargetMapper.toDomain(repository.save(DeliveryTargetMapper.toEntity(deliveryTarget)));
    }

    @Override
    public Optional<DeliveryTarget> getByJobId(UUID jobId) {
        return repository.findByJobId(jobId).map(DeliveryTargetMapper::toDomain);
    }
}
