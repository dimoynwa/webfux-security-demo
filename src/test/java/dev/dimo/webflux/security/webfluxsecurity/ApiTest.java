package dev.dimo.webflux.security.webfluxsecurity;

import dev.dimo.webflux.security.webfluxsecurity.config.RouterConfig;
import dev.dimo.webflux.security.webfluxsecurity.config.SecurityConfig;
import dev.dimo.webflux.security.webfluxsecurity.dto.TokenHolder;
import dev.dimo.webflux.security.webfluxsecurity.service.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import({RouterConfig.class, SecurityConfig.class, TokenService.class})
public class ApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiTest.class);

    @Autowired
    WebTestClient webTestClient;

    @Test
    void apiHelloWithUnauthenticatedShouldReturn401() {
        webTestClient.get().uri("/api/hello")
                .exchange().expectStatus().isUnauthorized();
    }

    @Test
    void testLoginWithRightCredentials() {
        TokenHolder responseBody = webTestClient
                .post()
                .uri("/api/login")
                .header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes()))
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody(TokenHolder.class).returnResult().getResponseBody();

        Assertions.assertNotNull(responseBody);
        LOGGER.info("Successful login, token generated: {}", responseBody.getToken());

        webTestClient.get().uri("/api/hello")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + responseBody.getToken())
                .exchange().expectStatus().isOk();
    }

}
