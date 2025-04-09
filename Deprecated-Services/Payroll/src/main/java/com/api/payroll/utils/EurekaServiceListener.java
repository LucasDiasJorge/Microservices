package com.api.payroll.utils;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class EurekaServiceListener implements ApplicationListener<ContextRefreshedEvent> {

    private final EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String applicationName;

    public EurekaServiceListener(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            // Aguardar um curto período para garantir que o serviço esteja registrado
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String serviceUrl = eurekaClient.getApplication(applicationName) != null ?
                eurekaClient.getApplication(applicationName).getInstances().stream()
                        .findFirst()
                        .map(instance -> "http://" + instance.getHostName() + ":" + instance.getPort())
                        .orElse("Service not registered in Eureka") :
                "Service not registered in Eureka";

        System.out.println("Service " + applicationName + " is available at: " + serviceUrl);
    }
}