package com.trace.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Allow access to login and auth endpoints
        if (path.startsWith("/login") || path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }

        // Check for JWT cookie
        HttpCookie jwtCookie = request.getCookies().getFirst("jwt");
        if (jwtCookie == null || jwtCookie.getValue().isEmpty()) {
            // Redirect to login if no valid token
            String redirectUri = request.getURI().toString();
            String loginUrl = "/login?redirect=" + redirectUri;

            exchange.getResponse().setStatusCode(HttpStatus.FOUND);
            exchange.getResponse().getHeaders().setLocation(java.net.URI.create(loginUrl));
            return exchange.getResponse().setComplete();
        }

        // Token is present, add to headers for downstream services
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("Authorization", "Bearer " + jwtCookie.getValue())
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}
