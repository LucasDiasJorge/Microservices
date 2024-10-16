package com.api.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${keycloak.auth.client-secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String role = claims.get("role", String.class);
                if (!isAuthorized(role, request.getRequestURI())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not Authorized");
                    return;
                }

            } catch (Exception e) {
                logger.error("JWT Token is invalid: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                return;
            }
        } else {
            logger.warn("Authorization header is missing or malformed");
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthorized(String role, String requestURI) {
        if (requestURI.startsWith("/root") && !"ROLE_ROOT".equals(role)) {
            return false;
        }
        return true;
    }
}
