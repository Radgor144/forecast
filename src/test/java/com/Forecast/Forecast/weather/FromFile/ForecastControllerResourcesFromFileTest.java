package com.Forecast.Forecast.weather.FromFile;

import com.Forecast.Forecast.util.JsonFileReader;
import com.Forecast.Forecast.util.StubUtil;
import com.Forecast.Forecast.weather.data.WeatherData;
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

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
class ForecastControllerResourcesFromFileTest {

    private static final String WEATHER_DATA_RESOURCE_PATH_TEMPLATE = "src/test/resources/%s.json";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;
    
    @ParameterizedTest(name = "{0}")
    @MethodSource("happyPathMethodSource")
    void happyPath(String name, String city) throws IOException {
        // given
        WeatherData expectedWeatherData = JsonFileReader.readJson(objectMapper, WEATHER_DATA_RESOURCE_PATH_TEMPLATE.formatted(city), WeatherData.class);

        // when
        var result = StubUtil.stubGetWeatherData(objectMapper, city, expectedWeatherData, webTestClient);

        // then
        result
                .expectBody(WeatherData.class)
                .consumeWith(response -> {
                            assertEquals(expectedWeatherData, response.getResponseBody());
                            response.getStatus().isSameCodeAs(HttpStatusCode.valueOf(200));
                        }
                );

    }

    private static Stream<Arguments> happyPathMethodSource() {
        return Stream.of(
                Arguments.of("Happy path for Kraków", "krakow"),
                Arguments.of("Happy path for Szczecin", "szczecin"),
                Arguments.of("Happy path for Kraków", "poznan")
        );
    }
}