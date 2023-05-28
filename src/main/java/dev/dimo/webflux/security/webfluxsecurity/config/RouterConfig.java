package dev.dimo.webflux.security.webfluxsecurity.config;

import dev.dimo.webflux.security.webfluxsecurity.dto.TokenHolder;
import dev.dimo.webflux.security.webfluxsecurity.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final TokenService tokenService;

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/api/hello", this::handleApiHello)
                .POST("/api/login", this::handleLogin)
                .build();
    }

    private Mono<ServerResponse> handleLogin(ServerRequest serverRequest) {
        return serverRequest.principal().flatMap(principal -> {
            if (principal instanceof Authentication basicAuth) {
                return ServerResponse.ok().bodyValue(new TokenHolder(tokenService.generateToken(basicAuth)));
            }
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        });
    }

    private Mono<ServerResponse> handleApiHello(ServerRequest serverRequest) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(user -> ServerResponse.ok().bodyValue(String.format("Hello, %s!", user.getName())));
    }

}