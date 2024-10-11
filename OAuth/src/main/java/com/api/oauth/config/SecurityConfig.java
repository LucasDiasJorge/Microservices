package com.api.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests -> {
                            authorizeRequests.requestMatchers(HttpMethod.GET,"/public").permitAll();
                            authorizeRequests.requestMatchers(HttpMethod.POST,"/auth").permitAll();
                            authorizeRequests.anyRequest().authenticated();
                        }
                ).oauth2ResourceServer(conf -> conf.jwt(Customizer.withDefaults()))
                .build();
    }

}