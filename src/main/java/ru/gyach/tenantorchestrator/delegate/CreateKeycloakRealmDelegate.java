package ru.gyach.tenantorchestrator.delegate;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gyach.tenantorchestrator.service.KeycloakService;

import static ru.gyach.tenantorchestrator.model.CreateTenantProcessVariables.*;

@Component
public class CreateKeycloakRealmDelegate implements JavaDelegate {

    @Autowired
    private KeycloakService keycloakService;

    @Override
    public void execute(DelegateExecution execution) throws ProcessEngineException {
        try {
            String slug = execution.getVariable(String.valueOf(TENANT_SLUG)).toString();
            String name = execution.getVariable(String.valueOf(TENANT_NAME)).toString();
            String ownerEmail = execution.getVariable(String.valueOf(TENANT_OWNER_EMAIL)).toString();
            String ownerPassword = execution.getVariable(String.valueOf(TENANT_OWNER_PASSWORD)).toString();

            keycloakService.createRealm(slug, name, ownerEmail, ownerPassword);

        } catch (Exception e) {
            throw new ProcessEngineException(e.getLocalizedMessage());
        }
    }
}
