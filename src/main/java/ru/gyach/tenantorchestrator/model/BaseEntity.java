package ru.gyach.tenantorchestrator.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Data
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "UUID")
    private UUID createdBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "UUID")
    private UUID updatedBy;

    @Column
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "UUID")
    private UUID deletedBy;

}
