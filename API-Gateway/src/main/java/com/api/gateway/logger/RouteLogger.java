package com.api.gateway.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RouteLogger {

    private static final Logger logger = LoggerFactory.getLogger(RouteLogger.class);

    private final RouteDefinitionLocator routeDefinitionLocator;

    public RouteLogger(RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * Roda no ApplicationReadyEvent e consome o Flux de forma reativa.
     * A versão anterior usava block() dentro de @PostConstruct, o que bloqueia
     * a thread de inicialização de uma aplicação reativa.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void logRoutes() {
        routeDefinitionLocator.getRouteDefinitions()
                .subscribe(route -> logger.info("Rota registrada: {} -> {}", route.getId(), route.getUri()));
    }
}
