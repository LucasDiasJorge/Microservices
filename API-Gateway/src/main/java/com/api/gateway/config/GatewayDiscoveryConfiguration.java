package com.api.gateway.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
public class GatewayDiscoveryConfiguration {

    @Bean
    public DiscoveryClientRouteDefinitionLocator
    discoveryClientRouteLocator(ReactiveDiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {

        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
    }
}