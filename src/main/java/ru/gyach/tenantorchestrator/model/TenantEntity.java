package ru.gyach.tenantorchestrator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.NaturalId;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "tenants")
@Data
@Accessors(chain = true)
public class TenantEntity extends BaseEntity {
    @NaturalId
    @Column(length = 8)
    @Comment("Human readable tenant id")
    private String slug;

    @Column(length = 64, nullable = false)
    @Comment("Tenant name")
    private String name;
}
