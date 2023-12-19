package ru.gyach.tenantorchestrator.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1.IngressBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;

@org.springframework.stereotype.Service
public class KubernetesService {
    private static final Logger logger = LoggerFactory.getLogger(KubernetesService.class);

    @Value("${ru.gyach.tenantorchestrator.kubernetes.master-node}")
    private String kubernetesMasterNode;

    @Value("${ru.gyach.tenantorchestrator.application-domain}")
    private String applicationDomain;

    /**
     * Подключение к kube API
     *
     * @return KubernetesClient
     */
    private KubernetesClient connect() {

        Config config = new ConfigBuilder().withMasterUrl(kubernetesMasterNode).build();

        return new KubernetesClientBuilder().withConfig(config).build();

    }

    /**
     * Создание Namespace
     * 
     * @param name Название неймспейса
     */
    public void createNamespace(String name) {
        try (KubernetesClient kubernetesClient = connect()) {
            Namespace namespace = new NamespaceBuilder()
                    .withNewMetadata()
                    .withName(name)
                    .endMetadata()
                    .build();

            namespace = kubernetesClient.namespaces().resource(namespace).create();
            logger.info("Created namespace: {}", namespace.getMetadata().getName());
        }
    }

    /**
     * Удаление Namespace
     * 
     * @param name Название неймспейса
     */
    public void deleteNamespace(String name) {
        try (KubernetesClient kubernetesClient = connect()) {
            kubernetesClient.namespaces().withName(name).delete();
            logger.info("Deleted namespace: {}", name);
        }
    }

    /**
     * Создание Deployment
     *
     * @param ns            Название неймспейса
     * @param appName       Название деплоймента
     * @param image         Образ
     * @param containerPort Порт контейнера, на котором выставлено приложение
     * @throws Exception
     */
    public void createDeployment(String ns, String appName, String image, Integer containerPort) throws Exception {

        try (KubernetesClient kubernetesClient = connect()) {

            Deployment deployment = new DeploymentBuilder()
                    .withNewMetadata()
                    .withName(appName + "-app")
                    .endMetadata()
                    .withNewSpec()
                    .withReplicas(1)
                    .withNewTemplate()
                    .withNewMetadata()
                    .addToLabels("app", appName)
                    .endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName(appName)
                    .withImage(image)
                    .addNewPort()
                    .withContainerPort(containerPort)
                    .endPort()
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .withNewSelector()
                    .addToMatchLabels("app", appName)
                    .endSelector()
                    .endSpec()
                    .build();

            deployment = kubernetesClient.apps().deployments().inNamespace(ns).resource(deployment).create();
            logger.info("Created deployment: {}", deployment.getMetadata().getName());

        } catch (Exception exception) {
            logger.error("createDeployment error: ", exception);
            throw new Exception(exception.getLocalizedMessage());
        } finally {

        }
    }

    public void createService(String namespace, String appName, Integer appPort, Integer servicePort) {
        try (KubernetesClient kubernetesClient = connect()) {
            Service service = new ServiceBuilder()
                    .withNewMetadata()
                    .withName(appName + "-service")
                    .endMetadata()
                    .withNewSpec()
                    .withSelector(Collections.singletonMap("app", appName))
                    .addNewPort()
                    .withName("http")
                    .withProtocol("TCP")
                    .withPort(servicePort)
                    .withTargetPort(new IntOrString(appPort))
                    .endPort()
                    .withType("ClusterIP")
                    .endSpec()
                    .build();

            service = kubernetesClient.services().inNamespace(namespace).resource(service).create();
            logger.info("Created service {}", service.getMetadata().getName());
        }
    }

    public void createIngress(String namespace, String appName, Integer appPort) {
        try (KubernetesClient kubernetesClient = connect()) {

            Ingress ingress = new IngressBuilder()
                    .withNewMetadata()
                    .withName(namespace + "-ingress")
                    .endMetadata()
                    .withNewSpec()
                    .withIngressClassName("nginx")
                    .addNewRule()
                    .withHost(namespace + "." + applicationDomain)
                    .withNewHttp()
                    .addNewPath().withPathType("Prefix")
                    .withPath("/")
                    .withNewBackend()
                    .withNewService()
                    .withName(appName + "-service")
                    .withNewPort()
                    .withNumber(appPort)
                    .endPort().endService().endBackend().endPath().endHttp().endRule().endSpec()
                    .build();

            ingress = kubernetesClient.network().v1().ingresses().inNamespace(namespace).resource(ingress).create();
            logger.info("Created ingress {}", ingress.getMetadata().getName());
        }
    }

    public void createFromFile(String namespace, String file) throws FileNotFoundException {
        try (KubernetesClient kubernetesClient = connect()) {

            // The class loader that loaded the class
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(file);

            System.out.println("getResourceAsStream : " + file);
            // printInputStream(inputStream);

            // the stream holding the file content
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found! " + file);
            } else {
                List<HasMetadata> result = kubernetesClient.load(inputStream).items();
                kubernetesClient.resourceList(result).inNamespace(namespace).serverSideApply();
            }

        }

    }
}
