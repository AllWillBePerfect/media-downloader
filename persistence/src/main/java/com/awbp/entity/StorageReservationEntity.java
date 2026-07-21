package com.awbp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "storage_reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageReservationEntity {

    @Id
    @Column(name = "job_id")
    private UUID jobId;

    @Column(name = "reserved_bytes", nullable = false)
    private Long reservedBytes;
}
