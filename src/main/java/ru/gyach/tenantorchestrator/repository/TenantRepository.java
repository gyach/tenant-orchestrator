package ru.gyach.tenantorchestrator.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.gyach.tenantorchestrator.model.TenantEntity;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
    TenantEntity findTenantBySlug(String slug);

    Boolean existsBySlug(String slug);

    void deleteBySlug(String slug);
}
