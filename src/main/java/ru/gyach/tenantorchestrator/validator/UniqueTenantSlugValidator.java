package ru.gyach.tenantorchestrator.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gyach.tenantorchestrator.annotation.UniqueTenantSlug;
import ru.gyach.tenantorchestrator.repository.TenantRepository;

@Component
@RequiredArgsConstructor
public class UniqueTenantSlugValidator implements ConstraintValidator<UniqueTenantSlug, String> {

    private final TenantRepository tenantRepository;

    @Override
    public boolean isValid(String slug, ConstraintValidatorContext constraintValidatorContext) {
        return !tenantRepository.existsBySlug(slug);
    }
}
