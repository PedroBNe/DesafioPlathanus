    package com.pedrobne.apigateway.filter;

    import com.pedrobne.apigateway.dto.AuthValidationResponse;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cloud.gateway.filter.GatewayFilterChain;
    import org.springframework.cloud.gateway.filter.GlobalFilter;
    import org.springframework.core.Ordered;
    import org.springframework.core.annotation.Order;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;
    import org.springframework.stereotype.Component;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.reactive.function.client.WebClient;
    import org.springframework.web.server.ServerWebExchange;
    import reactor.core.publisher.Mono;

    import java.util.List;
    import java.util.Map;

    @Component
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public class AuthFilter implements GlobalFilter {

        private static final List<String> PUBLIC_ENDPOINTS = List.of(
                "/usuario/autenticar"
        );

        private static final Map<String, String> PROTECTED_ENDPOINTS = Map.of(
                "/usuario/list", "VENDEDOR",
                "/sale/physical", "VENDEDOR",
                "/sale/online", "CLIENTE"
        );

        private final WebClient.Builder webClientBuilder;

        @Autowired
        public AuthFilter(WebClient.Builder webClientBuilder) {
            this.webClientBuilder = webClientBuilder;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
            String path = exchange.getRequest().getURI().getPath();

            if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
                return chain.filter(exchange);
            }

            if (isPublicEndpoint(path)) {
                return chain.filter(exchange);
            }

            String token = null;

            List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeaders != null && !authHeaders.isEmpty()) {
                token = authHeaders.getFirst().replace("Bearer ", "");
            }

            if (token == null || token.isBlank()) {
                token = exchange.getRequest().getQueryParams().getFirst("token");
            }

            if (token == null || token.isBlank()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClientBuilder.build()
                    .get()
                    .uri("lb://auth-service/usuario/validar-token?token=" + token)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        return Mono.error(new RuntimeException("Invalid token"));
                    })
                    .bodyToMono(AuthValidationResponse.class)
                    .flatMap(response -> {
                        if (!response.authenticated()) {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }

                        String requiredRole = getRequiredRole(path);
                        if (requiredRole != null && !requiredRole.equalsIgnoreCase(response.role())) {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(builder -> builder
                                        .header("X-User-Login", response.login())
                                        .header("X-User-Role", response.role())
                                )
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        private boolean isPublicEndpoint(String path) {
            return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith);
        }

        private String getRequiredRole(String path) {
            return PROTECTED_ENDPOINTS.entrySet().stream()
                    .filter(entry -> path.startsWith(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null);
        }
    }