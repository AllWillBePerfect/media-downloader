package com.awbp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "storage_usage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageUsageEntity {

    @Id
    private Integer id;

    @Column(name = "limit_bytes", nullable = false)
    private Long limitBytes;

    @Column(name = "used_bytes", nullable = false)
    private Long usedBytes;

    @Column(name = "reserved_bytes", nullable = false)
    private Long reservedBytes;
}