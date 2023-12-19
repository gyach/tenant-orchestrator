package ru.gyach.tenantorchestrator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление процессов Camunda
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CamundaProcessesEnum {
    /**
     * Процесс создания тенанта
     */
    CREATE_TENANT_PROCESS("CreateTenantProcess"),
    /**
     * Процесс создания тенанта
     */
    CREATE_K8S_NAMESPACE_PROCESS("CreateKubernetesNamespaceProcess");

    /**
     * ID процесса
     */
    private final String processId;
}
