package ru.gyach.tenantorchestrator.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import ru.gyach.tenantorchestrator.annotation.UniqueTenantSlug;


@Data
@Accessors(chain = true)
public class CreateTenantRequestDto {
    @Size(min = 1, max = 8)
    @Pattern(regexp = "^[a-z0-9_]*$", message = "regexp pattern ^[a-z0-9_]*$")
    @UniqueTenantSlug()
    @NotNull
    private String slug;

    @Length(min = 1, max = 64)
    @NotNull
    private String name;

    @Email
    @NotNull
    private String ownerEmail;

}
