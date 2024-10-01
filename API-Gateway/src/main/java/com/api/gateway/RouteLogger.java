package com.api.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Component
public class RouteLogger {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @PostConstruct
    public void logRoutes() {
        List<RouteDefinition> routes = routeDefinitionLocator.getRouteDefinitions().collectList().block();
        System.out.println("Available Routes:");
        routes.forEach(route -> System.out.println(route.getId() + ": " + route.getUri()));
    }
}
