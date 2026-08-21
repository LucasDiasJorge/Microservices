package com.api.payroll.utils;

import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Loga onde o serviço ficou disponível depois que ele sobe.
 *
 * A versão anterior dormia 15s dentro do ContextRefreshedEvent, atrasando
 * a inicialização de toda a aplicação. ApplicationReadyEvent já dispara
 * depois que o contexto está pronto, sem precisar de sleep.
 */
@Component
public class EurekaServiceListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaServiceListener.class);

    private final EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String applicationName;

    public EurekaServiceListener(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logRegistration() {
        var application = eurekaClient.getApplication(applicationName);
        if (application == null || application.getInstances().isEmpty()) {
            logger.info("Serviço {} ainda não registrado no Eureka", applicationName);
            return;
        }
        application.getInstances().forEach(instance ->
                logger.info("Serviço {} disponível em http://{}:{}",
                        applicationName, instance.getHostName(), instance.getPort()));
    }
}
