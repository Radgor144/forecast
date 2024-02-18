package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import com.Forecast.Forecast.weather.fixtures.WeatherDataFixture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
class ForecastControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    static Stream<Arguments> provideAddressesAndCities() {
        return Stream.of(
                Arguments.of("Kraków, Woj. Małopolskie, Polska", "Krakow"),
                Arguments.of("Szczecin, Woj. Zachodniopomorskie, Polska", "Szczecin"),
                Arguments.of("Poznań, Woj. Wielkopolskie, Polska", "Poznan")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAddressesAndCities")
    void happyPath(String address, String city) throws JsonProcessingException {
//      given
        var weatherData = WeatherDataFixture.defaultWeatherData(address);

        stubFor(get(urlEqualTo("/VisualCrossingWebServices/rest/services/timeline/" + city + "?unitGroup=metric&include=hours%2Cdays&key=FAKE_API_KEY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(weatherData)))
        );
//      when
        var result = webTestClient
                .get()
                .uri("/forecast/" + city)
                .exchange();
//      then
        result
                .expectBody(WeatherData.class)
                .consumeWith(response -> {
                            assertEquals(weatherData, response.getResponseBody());
                            response.getStatus().isSameCodeAs(HttpStatusCode.valueOf(200));
                        }
                );
    }
}