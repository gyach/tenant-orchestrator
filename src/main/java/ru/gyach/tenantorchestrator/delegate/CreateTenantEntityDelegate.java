package ru.gyach.tenantorchestrator.delegate;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.gyach.tenantorchestrator.dto.CreateTenantRequestDto;
import ru.gyach.tenantorchestrator.service.TenantService;

import static ru.gyach.tenantorchestrator.model.CreateTenantProcessVariables.*;

/**
 * Делегат создания сущности тенанта в БД Core
 */
@Component
public class CreateTenantEntityDelegate implements JavaDelegate {

    @Autowired
    private TenantService tenantService;

    /**
     * Обработчик ServiceTask'а
     * @param execution Доступ к контексту процесса
     * @throws ProcessEngineException Инцидент камунды
     */
    @Override
    public void execute(DelegateExecution execution) throws ProcessEngineException {
        try {
            CreateTenantRequestDto dto = new CreateTenantRequestDto()
                    .setSlug(execution.getVariable(String.valueOf(TENANT_SLUG)).toString())
                    .setName(execution.getVariable(String.valueOf(TENANT_NAME)).toString())
                    .setOwnerEmail(execution.getVariable(String.valueOf(TENANT_OWNER_EMAIL)).toString());

            tenantService.createTenant(dto);

        } catch (Exception e) {
            throw new ProcessEngineException(e.getLocalizedMessage());
        }
    }

}
