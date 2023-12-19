package ru.gyach.tenantorchestrator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.gyach.tenantorchestrator.validator.UniqueTenantSlugValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueTenantSlugValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueTenantSlug {

    String message() default "Tenant slug is taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
