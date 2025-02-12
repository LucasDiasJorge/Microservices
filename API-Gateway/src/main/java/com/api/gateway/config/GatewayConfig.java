package com.api.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        Map<String, String> routes = new HashMap<>();
        routes.put("/oauth/auth", "/auth");
        routes.put("/oauth/create", "/create");
        routes.put("/oauth/validate", "/validate");

        RouteLocatorBuilder.Builder routesBuilder = builder.routes();

        routes.forEach((path, rewritePath) ->
                routesBuilder.route(
                        path.replace("/", "_") + "_route", // Nome da rota (ex: keycloak_auth_route)
                        r -> r.path(path)
                                .filters(f -> f.rewritePath(path, rewritePath))
                                .uri("lb://OAUTH-SERVICE")
                ));

        return routesBuilder.build();
    }
}