package com.api.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RequestResponseLoggingFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        logger.info("Request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());

        return chain.filter(exchange).doOnSuccess(aVoid -> {
            logger.info("Response Status: {}", exchange.getResponse().getStatusCode());
        });
    }
}
