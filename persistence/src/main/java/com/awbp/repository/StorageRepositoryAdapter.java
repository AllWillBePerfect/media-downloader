package com.awbp.repository;

import com.awbp.entity.StorageReservationEntity;
import com.awbp.entity.StorageUsageEntity;
import com.awbp.ports.StorageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StorageRepositoryAdapter implements StorageRepository {

    private final StorageUsageJpaRepository usageRepository;
    private final StorageReservationJpaRepository reservationRepository;

    @Override
    @Transactional
    public boolean tryReserve(UUID jobId, long bytes) {
        if (bytes <= 0) {
            throw new IllegalArgumentException("Размер должен быть больше нуля");
        }

        if (reservationRepository.existsById(jobId)) {
            return true;
        }

        StorageUsageEntity usage = usageRepository.findGlobalForUpdate()
                .orElseThrow();

        long occupiedBytes = usage.getUsedBytes() + usage.getReservedBytes();

        if (occupiedBytes + bytes > usage.getLimitBytes()) {
            return false;
        }

        usage.setReservedBytes(usage.getReservedBytes() + bytes);

        reservationRepository.save(
                new StorageReservationEntity(jobId, bytes)
        );

        return true;
    }

    @Override
    @Transactional
    public void commit(UUID jobId, long actualBytes) {
        StorageUsageEntity usage = usageRepository.findGlobalForUpdate()
                .orElseThrow();

        StorageReservationEntity reservation = reservationRepository.findById(jobId)
                .orElseThrow();

        usage.setReservedBytes(
                usage.getReservedBytes() - reservation.getReservedBytes()
        );
        usage.setUsedBytes(
                usage.getUsedBytes() + actualBytes
        );

        reservationRepository.delete(reservation);
    }

    @Override
    @Transactional
    public void release(UUID jobId) {
        StorageUsageEntity usage = usageRepository.findGlobalForUpdate()
                .orElseThrow();

        reservationRepository.findById(jobId).ifPresent(reservation -> {
            usage.setReservedBytes(
                    usage.getReservedBytes() - reservation.getReservedBytes()
            );

            reservationRepository.delete(reservation);
        });
    }
}
