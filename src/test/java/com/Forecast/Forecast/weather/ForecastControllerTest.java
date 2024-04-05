package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.util.StubUtil;
import com.Forecast.Forecast.weather.data.WeatherData;
import com.Forecast.Forecast.weather.fixtures.WeatherDataFixture;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static com.Forecast.Forecast.util.RequestUtil.getForecastRequest;
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
        StubUtil.stubGetWeatherData(objectMapper, city, weatherData);
//      when
        var result = getForecastRequest(webTestClient, city);

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