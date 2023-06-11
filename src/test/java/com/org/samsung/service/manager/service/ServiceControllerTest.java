package com.org.samsung.service.manager.service;

import com.org.samsung.service.manager.util.UtilsTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
public class ServiceControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    /* Test external APIs concurrently and check result on console without running the application*/
    @Test
    public void testInvokeEndpoint() throws Exception {
        Flux<String> msg$ = webTestClient.post()
                .uri("/test/service-manager/invoke")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(UtilsTest.getInvokePayload())
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody()
                .log();

        StepVerifier.create(msg$)
                .expectNextCount(1)
                .verifyComplete();
    }

    /* Test external APIs concurrently and check result on console without running the application*/
    @Test
    public void testGetSupportedParametersEndpoint() {
        Flux<String> msg$ = webTestClient.get()
                .uri("/test/service-manager/supported-attributes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody()
                .log();

        StepVerifier.create(msg$)
                .expectNextCount(1)
                .verifyComplete();
    }

    /* Test external APIs concurrently and check result on console without running the application*/
    @Test
    public void testGetMaxConcurrencyEndpoint() {
        Flux<String> msg$ = webTestClient.get()
                .uri("/test/service-manager/max-concurrency")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class).getResponseBody()
                .log();

        StepVerifier.create(msg$)
                .expectNextCount(1)
                .verifyComplete();
    }
}
