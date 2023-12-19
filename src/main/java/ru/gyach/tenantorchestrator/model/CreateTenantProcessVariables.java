package ru.gyach.tenantorchestrator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление переменных процесса создания тенанта {@link CamundaProcessesEnum#CREATE_TENANT_PROCESS}
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CreateTenantProcessVariables {
    /**
     * SLUG тенанта
     */
    TENANT_SLUG("tenantSlug"),
    /**
     * Название тенанта
     */
    TENANT_NAME("tenantName"),
    /**
     * Email первого пользователя - администратора тенанта
     */
    TENANT_OWNER_EMAIL("tenantOwnerEmail"),
    /**
     * Пароль первого пользователя - администратора тенанта
     */
    TENANT_OWNER_PASSWORD("tenantOwnerPassword");


    /**
     * Название переменной процесса
     */
    private final String variableName;
}
