package ru.gyach.tenantorchestrator.controller;

import jakarta.validation.Valid;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gyach.tenantorchestrator.dto.CreateTenantRequestDto;
import ru.gyach.tenantorchestrator.dto.StartedProcessResponseDto;
import ru.gyach.tenantorchestrator.service.KubernetesService;

import java.io.FileNotFoundException;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static ru.gyach.tenantorchestrator.model.CamundaProcessesEnum.CREATE_TENANT_PROCESS;
import static ru.gyach.tenantorchestrator.model.CreateTenantProcessVariables.*;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantsController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private KubernetesService kubernetesService;

    /**
     * Запускает в камунде процесс создания тенанта
     * {@link ru.gyach.tenantorchestrator.model.CamundaProcessesEnum#CREATE_TENANT_PROCESS}
     *
     * @param dto {@link CreateTenantRequestDto}
     * @return {@link StartedProcessResponseDto}
     */
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<StartedProcessResponseDto> createTenant(@RequestBody @Valid CreateTenantRequestDto dto) {

        ProcessInstance process = runtimeService
                .createProcessInstanceByKey(String.valueOf(CREATE_TENANT_PROCESS))
                .businessKey(dto.getSlug())
                .setVariable(String.valueOf(TENANT_SLUG), dto.getSlug())
                .setVariable(String.valueOf(TENANT_NAME), dto.getName())
                .setVariable(String.valueOf(TENANT_OWNER_EMAIL), dto.getOwnerEmail())
                .setVariable(String.valueOf(TENANT_OWNER_PASSWORD), UUID.randomUUID().toString())
                .execute();

        StartedProcessResponseDto responseDto = new StartedProcessResponseDto();
        responseDto.setProcessId(process.getProcessInstanceId());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> getProcessStatus() {

        try {
            kubernetesService.createFromFile("default", "k8s/test.yaml");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ResponseEntity.ok("ok");
    }

}
