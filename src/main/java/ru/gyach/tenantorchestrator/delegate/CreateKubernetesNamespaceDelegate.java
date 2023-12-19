package ru.gyach.tenantorchestrator.delegate;

import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gyach.tenantorchestrator.service.KubernetesService;

import static ru.gyach.tenantorchestrator.model.CreateTenantProcessVariables.TENANT_SLUG;

@Component
@AllArgsConstructor
public class CreateKubernetesNamespaceDelegate implements JavaDelegate {

    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public void execute(DelegateExecution execution) throws ProcessEngineException {
        String slug = execution.getVariable(String.valueOf(TENANT_SLUG)).toString();
        kubernetesService.createNamespace(slug);
    }
}
