package com.trace.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtGatewayFilter extends AbstractGatewayFilterFactory<JwtGatewayFilter.Config> {

    public JwtGatewayFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String jwt = null;

            // Try to get JWT from cookie
            String cookieHeader = exchange.getRequest().getHeaders().getFirst("Cookie");
            if (StringUtils.hasText(cookieHeader)) {
                String[] cookies = cookieHeader.split(";");
                for (String cookie : cookies) {
                    cookie = cookie.trim();
                    if (cookie.startsWith("jwt=")) {
                        jwt = cookie.substring(4);
                        break;
                    }
                }
            }

            ServerHttpRequest request = exchange.getRequest();
            if (StringUtils.hasText(jwt)) {
                // Add JWT to Authorization header
                request = request.mutate()
                    .header("Authorization", "Bearer " + jwt)
                    .build();
            }

            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
        // Configuration properties if needed
    }
}
