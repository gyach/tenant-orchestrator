package ru.gyach.tenantorchestrator.delegate;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gyach.tenantorchestrator.service.KubernetesService;

import static ru.gyach.tenantorchestrator.model.CreateTenantProcessVariables.TENANT_SLUG;

@Component
public class DeployFooAppDelegate implements JavaDelegate {

    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public void execute(DelegateExecution execution) throws ProcessEngineException {
        String slug = execution.getVariable(String.valueOf(TENANT_SLUG)).toString();
        String appName = "foo";
        try {
            kubernetesService.createDeployment(slug, appName, "mickeyyawn/foo:latest", 8080);
            kubernetesService.createService(slug,appName, 8080, 8080);
            kubernetesService.createIngress(slug, appName, 8080);
        } catch (Exception e) {
            throw new ProcessEngineException(e.getLocalizedMessage());
        }
    }
}
