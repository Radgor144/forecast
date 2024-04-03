package com.Forecast.Forecast.cors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
public class ControllerCorsTest {

    public static final String URL = "/VisualCrossingWebServices/rest/services/timeline/XXX?unitGroup=metric&include=hours%2Cdays&key=FAKE_API_KEY";

    @Autowired
    private WebTestClient webTestClient;


    @ParameterizedTest
    @MethodSource("originToTest")
    void corsTest(String origin, int expectedStatus) {
        //given
        stubFor(get(urlEqualTo(URL))
                .willReturn(aResponse()
                        .withStatus(expectedStatus))
        );

        //when
        var result = webTestClient
                .get()
                .uri("/forecast/XXX")
                .header("Origin", origin)
                .exchange();

        //then
        result.expectStatus().isEqualTo(expectedStatus);
    }

    static Stream<Arguments> originToTest() {
        return Stream.of(
                // Test valid origins with different ports and protocols
                Arguments.of("http://localhost:5656", 200),
                Arguments.of("https://example.com", 403),
                Arguments.of("https://subdomain.example.com", 403),
                Arguments.of("https://localhost", 403),

                // Test localhost with different ports (valid and invalid)
                Arguments.of("http://localhost:8080", 200),
                Arguments.of("http://localhost:3000", 200),
                Arguments.of("http://localhost:12345", 200),

                // Test localhost with different subdomains (valid and invalid)
                Arguments.of("http://valid.localhost", 403),
                Arguments.of("http://invalid.localhost", 403),
                Arguments.of("http://subdomain.localhost:8080", 403),

                // Test loopback IP address
                Arguments.of("http://127.0.0.1", 403),
                Arguments.of("http://[::1]", 403), // IPv6 loopback address

                // Test invalid or unreachable origins
                Arguments.of("http://randomOrigin", 403),
                Arguments.of("http://randomOrigin:5", 403),
                Arguments.of("https://abc:8080", 403),
                Arguments.of("https://abc:123", 403),

                // Test null or empty origin headers
                //Arguments.of(null, 403),
                Arguments.of("", 403)
        );
    }

}
