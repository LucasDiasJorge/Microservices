package com.api.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        return http
                .authorizeExchange(
                        exchange -> {
                            exchange.pathMatchers(HttpMethod.POST, "/oauth/auth").permitAll().anyExchange().authenticated();
                        }
                )
                .build();
    }

}