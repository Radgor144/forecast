package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

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
    private static final String STRING_WITH_41_CHARS = "abcdefghijklmnopqrstuvwxyzabcdefghijklmno";
    public static final String EMPTY_CITY_NAME_ERROR_MESSAGE = "Error while user entered empty city name";
    public static final String EMPTY_CITY_NAME_ERROR_REASON = "getWeatherData.city: wielkość musi należeć do zakresu od 2 do 40";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest(name = "{1}")
    @MethodSource("shouldMapExternalErrorCodeToInternalErrorCodeMethodSource")
    void shouldMapExternalErrorCodeToInternalErrorCode(String reason, int httpCode, int expectedHttpCode) {
        //given
        stubFor(get(urlEqualTo(URL))
                .willReturn(aResponse()
                        .withStatus(httpCode)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(reason))
        );

        //when
        var result = webTestClient
                .get()
                .uri("/forecast/XXX")
                .exchange();

        //then
        result
                .expectStatus().isEqualTo(expectedHttpCode)
                .expectBody(ErrorResponse.class)
                .isEqualTo(new ErrorResponse(ERROR_MESSAGE, reason, expectedHttpCode));
    }

    @ParameterizedTest
    @MethodSource("shouldReturn400ErrorWhenInvalidParameterPassedToGetWeatherErrorCodeMethodSource")
    public void shouldReturn400ErrorWhenInvalidParameterPassedToGetWeather(String uri, String message, String reason) {
        //given
        //when
        var result = webTestClient
                .get()
                .uri("/forecast/" + uri)
                .exchange();

        //then
        result
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorResponse.class)
                .isEqualTo(new ErrorResponse(message, reason, 400));
    }


    private static Stream<Arguments> shouldMapExternalErrorCodeToInternalErrorCodeMethodSource() {
        return Stream.of(
                Arguments.of("Bad API Request:Invalid location parameter value.", 400, 400),
                Arguments.of("No account found with API key 'fake_api_key'", 401, 401),
                Arguments.of("Not found weather data", 404, 404),
                Arguments.of("Too many requests", 429, 429),
                Arguments.of("Bad gateway", 502, 502),
                Arguments.of("Unexpected external error 502", 418, 502)
        );
    }private static Stream<Arguments> shouldReturn400ErrorWhenInvalidParameterPassedToGetWeatherErrorCodeMethodSource() {
        return Stream.of(
                Arguments.of(" ", EMPTY_CITY_NAME_ERROR_MESSAGE, "getWeatherData.city: nie może być odstępem, getWeatherData.city: wielkość musi należeć do zakresu od 2 do 40"),
                Arguments.of(STRING_WITH_41_CHARS, EMPTY_CITY_NAME_ERROR_MESSAGE, EMPTY_CITY_NAME_ERROR_REASON),
                Arguments.of("a", EMPTY_CITY_NAME_ERROR_MESSAGE, EMPTY_CITY_NAME_ERROR_REASON)
        );
    }
}