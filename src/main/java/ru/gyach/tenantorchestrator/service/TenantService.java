package ru.gyach.tenantorchestrator.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.gyach.tenantorchestrator.dto.CreateTenantRequestDto;
import ru.gyach.tenantorchestrator.model.TenantEntity;
import ru.gyach.tenantorchestrator.repository.TenantRepository;

/**
 * Управление сущностью тенанта
 */
@Service
public class TenantService {
    @Autowired private TenantRepository tenantRepository;
    @Autowired private ModelMapper modelMapper;

    /**
     * Создает в БД сущность тенанта
     *
     * @param dto {@link CreateTenantRequestDto}
     * @throws Exception
     */
    public void createTenant(CreateTenantRequestDto dto) throws Exception {
        try {
            TenantEntity tenant = modelMapper.map(dto, TenantEntity.class);
            tenantRepository.save(tenant);
        } catch (Exception e) {
            throw new Exception(e);
        }

    }
}
