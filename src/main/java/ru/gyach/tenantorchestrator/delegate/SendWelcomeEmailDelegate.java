package ru.gyach.tenantorchestrator.delegate;

import jakarta.mail.internet.MimeMessage;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import static ru.gyach.tenantorchestrator.model.CreateTenantProcessVariables.*;

@Component
public class SendWelcomeEmailDelegate implements JavaDelegate {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${ru.gyach.tenantorchestrator.application-domain}")
    private String applicationDomain;

    @Override
    public void execute(DelegateExecution execution) throws ProcessEngineException {
        try {

            String slug = execution.getVariable(String.valueOf(TENANT_SLUG)).toString();
            String name = execution.getVariable(String.valueOf(TENANT_NAME)).toString();
            String ownerEmail = execution.getVariable(String.valueOf(TENANT_OWNER_EMAIL)).toString();
            String ownerPassword = execution.getVariable(String.valueOf(TENANT_OWNER_PASSWORD)).toString();

            if (ownerPassword == null || ownerPassword.isEmpty())
                throw new ProcessEngineException("Owner password is empty!");

            // fixme вынести в отдельный сервис
            String from = slug + "@gyach.ru";
            String to = ownerEmail;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("Tenant " + name + " created");
            helper.setFrom(from);
            helper.setTo(to);

            String content = "<h1>Тенант создан</h1>"
                    + "<p>Ссылка для входа: <a href=\"https://" + slug + "." + applicationDomain + "\">https://" + slug + "." + applicationDomain + "</a></p>"
                    + "<p>Войти на портал можно с логином: <b>" + ownerEmail + "</b></p>"
                    + "<p>Ваш временный пароль: <b>" + ownerPassword + "</b></p>";
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new ProcessEngineException(e.getLocalizedMessage());
        }
    }
}
