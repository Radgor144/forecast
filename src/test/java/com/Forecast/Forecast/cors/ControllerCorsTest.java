package com.Forecast.Forecast.cors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;
@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ControllerCorsTest {

    @Autowired
    private WebTestClient webTestClient;

    @ParameterizedTest
    @MethodSource("shouldAllowValidOriginMethodSource")
    void shouldAllowValidOrigin(String origin) {

        // given & when
        var response = webTestClient
                .options()
                .uri("/forecast/xxx")
                .header("Origin", origin)
                .exchange();

        //then

        response.expectHeader().valueEquals("Access-Control-Allow-Origin", origin)
                .expectHeader().valueEquals("Allow", "GET,HEAD,OPTIONS")
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @MethodSource("shouldDenyInvalidOriginMethodSource")
    void shouldDenyInvalidOrigin(String origin) {

        // given & when
        var response = webTestClient
                .options()
                .uri("/forecast/xxx")
                .header("Origin", origin)
                .exchange();

        //then

        response
                .expectStatus().isForbidden()
                .expectBody(String.class).isEqualTo("Invalid CORS request");
    }

    static Stream<Arguments> shouldAllowValidOriginMethodSource() {
        return Stream.of(
                Arguments.of("http://localhost:5656"),
                Arguments.of("http://localhost:8080"),
                Arguments.of("http://localhost:3000"),
                Arguments.of("http://localhost:12345")
        );
    }

    static Stream<Arguments> shouldDenyInvalidOriginMethodSource() {
        return Stream.of(
                Arguments.of("https://example.com"),
                Arguments.of("http://valid.localhost"),
                Arguments.of("http://invalid.localhost"),
                Arguments.of("http://subdomain.localhost:8080"),

                // Test loopback IP address
                Arguments.of("http://127.0.0.1"),
                Arguments.of("http://[::1]"), // IPv6 loopback address

                // Test invalid or unreachable origins
                Arguments.of("http://randomOrigin"),
                Arguments.of("http://randomOrigin:5"),
                Arguments.of("https://abc:8080"),
                Arguments.of("https://abc:123"),

                // Test null or empty origin headers
                Arguments.of("")
        );
    }
}
