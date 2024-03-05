package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@Slf4j
public class ErrorTest {

    public static final String URL = "/VisualCrossingWebServices/rest/services/timeline/XXX?unitGroup=metric&include=hours%2Cdays&key=FAKE_API_KEY";
    public static final String ERROR_MESSAGE = "Error while connecting to weather client API.";
    private static final List<Integer> ACCEPTED_CODES = List.of(400, 401, 404, 429, 502);
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    static Stream<Arguments> provideErrorCodes() {
        return Stream.of(
                Arguments.of("Bad API Request:Invalid location parameter value.", 400),
                Arguments.of("No account found with API key 'fake_api_key'", 401),
                Arguments.of("Not found weather data", 404),
                Arguments.of("Too many requests", 429),
                Arguments.of("Bad gateway", 502),
                Arguments.of("Unexpected external error 502", 418)
        );
    }

    @ParameterizedTest(name = "{1}")
    @MethodSource("provideErrorCodes")
    void getErrors(String reason, int HTTPCode) throws Exception {
        //given
        if (!ACCEPTED_CODES.contains(HTTPCode)) {
            HTTPCode = 502;
        }
        ErrorResponse errorResponse = new ErrorResponse(ERROR_MESSAGE, reason, HTTPCode);
        log.info("Error Decoder: {}", objectMapper.writeValueAsString(errorResponse));
        stubFor(get(urlEqualTo(URL))
                .willReturn(aResponse()
                        .withStatus(HTTPCode)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(reason))
        );

        //when
        var result = webTestClient
                .get()
                .uri("/forecast/XXX")
                .exchange()
                .expectStatus().isEqualTo(HTTPCode)
                .expectBody(ErrorResponse.class);

        //Then
        result
                .isEqualTo(errorResponse);
    }
}
