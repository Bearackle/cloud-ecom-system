package com.dinhhuan.filter;

import com.dinhhuan.configuration.IgnoreUrls;
import com.dinhhuan.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.ErrorResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;


@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private IgnoreUrls ignoreUrls;
    @Value("${auth.service.url}")
    private String authServiceUrl;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        AntPathMatcher matcher = new AntPathMatcher();
        boolean isIgnored = ignoreUrls.getUrls().stream()
                .anyMatch(pattern -> matcher.match(request.getPath().toString(), pattern));
        if (isIgnored) {
            return chain.filter(exchange);
        }
        String token = extractToken(request);
        if (token == null) {
            return onError(exchange, "Missing authorization token", HttpStatus.UNAUTHORIZED);
        }
        return webClientBuilder.build()
                .post()
                .uri(authServiceUrl + "/auth/introspect")
                .header("Authorization", "Bearer " + token)
                .bodyValue(Map.of("token", token))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .flatMap(authResponse -> {
                    if (authResponse.getIsAuthenticated()) {
                        ServerHttpRequest modifiedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", authResponse.getUserId().toString())
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } else {
                        return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                    }
                })
                .onErrorResume(error -> onError(exchange, "Authentication service error" + error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }
    @Override
    public int getOrder() {
        return -1;
    }
    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                status,
                message
        );
        byte[] bytes;
        try {
            bytes = new ObjectMapper().writeValueAsBytes(problemDetail);
        } catch (Exception e) {
            bytes = new byte[0];
        }
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
