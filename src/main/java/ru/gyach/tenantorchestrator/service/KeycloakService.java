package ru.gyach.tenantorchestrator.service;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${keycloak.admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.admin.password}")
    private String keycloakAdminPassword;

    @Value("${keycloak.admin.server-url}")
    private String keycloakAdminServerUrl;

    @Value("${keycloak.admin.realm}")
    private String keycloakAdminRealm;

    @Value("${keycloak.admin.client-id}")
    private String keycloakAdminClientId;

    private static final Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    public void createRealm(String slug, String name, String ownerEmail, String ownerPassword) throws Exception {

        try {
            // Создаем инстанс
            Keycloak keycloak = Keycloak.getInstance(
                    keycloakAdminServerUrl,
                    keycloakAdminRealm,
                    keycloakAdminUsername,
                    keycloakAdminPassword,
                    keycloakAdminClientId);

            // Наполняем мясом сущность realm
            RealmRepresentation realm = prepareRealmRepresentation(slug, name);

            // создаем realm
            keycloak.realms().create(realm);

            // Наполняем мясом сущность владельца реалма
            UserRepresentation owner = prepareUserRepresentation(ownerEmail, ownerPassword);

            // Создаем владельца
            keycloak.realm(slug).users().create(owner);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    private static RealmRepresentation prepareRealmRepresentation(String slug, String name) {
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(slug);
        realm.setDisplayName(name);
        realm.setRegistrationEmailAsUsername(true);
        realm.setLoginWithEmailAllowed(true);
        realm.setEnabled(true);
        // Настройки SMTP-сервера
        Map<String, String> smtpServer = new HashMap<>();
        smtpServer.put("host", "smtp4dev");
        smtpServer.put("from", slug + "@gyach.ru");
        realm.setSmtpServer(smtpServer);
        logger.info(realm.toString());
        return realm;
    }

    private static UserRepresentation prepareUserRepresentation(String ownerEmail, String ownerPassword) {
        UserRepresentation owner = new UserRepresentation();
        owner.setEmail(ownerEmail);
        owner.setEnabled(true);

        // Создаем для него первоначальный пароль
        CredentialRepresentation ownerCredPass = new CredentialRepresentation();
        ownerCredPass.setType(CredentialRepresentation.PASSWORD);
        ownerCredPass.setValue(ownerPassword);
        ownerCredPass.setTemporary(true);
        ownerCredPass.setUserLabel("User password");
        List<CredentialRepresentation> ownerCreds = new ArrayList<>();
        ownerCreds.add(ownerCredPass);
        owner.setCredentials(ownerCreds);
        return owner;
    }
}
