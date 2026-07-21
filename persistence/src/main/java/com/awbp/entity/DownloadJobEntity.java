package com.awbp.entity;

import com.awbp.domain.DeliveryType;
import com.awbp.domain.DownloadStatus;
import com.awbp.domain.DownloadType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "download_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadJobEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "url", nullable = false)
    private String url;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DownloadType type;

    /*@Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType;*/

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DownloadStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
}
