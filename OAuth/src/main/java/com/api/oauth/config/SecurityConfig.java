package com.api.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorizeRequests -> {
                            authorizeRequests.requestMatchers("/public").permitAll();
                            authorizeRequests.requestMatchers("/logout").permitAll();
                            authorizeRequests.anyRequest().authenticated();
                        }
                ).oauth2ResourceServer(conf -> conf.jwt(Customizer.withDefaults()))
                .build();
    }

}