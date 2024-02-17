package com.Forecast.Forecast.weather.FromFile;

import com.Forecast.Forecast.weather.data.WeatherData;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.File;
import java.io.IOException;
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
class ForecastControllerFromFileTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    WeatherData getJson(String city) {
        WeatherData weatherData = null;
        objectMapper.registerModule(new JavaTimeModule());
        try {
            weatherData = objectMapper.readValue(new File("src/test/resources/" + city + ".json"), WeatherData.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherData;

    }

    private static Stream<String> provideCities() {
        return Stream.of("krakow", "szczecin", "poznan");
    }

    @ParameterizedTest
    @MethodSource("provideCities")
    void happyPath(String city) throws IOException {
        // given
        WeatherData weatherData = getJson(city);

        stubFor(get(urlEqualTo("/VisualCrossingWebServices/rest/services/timeline/" + city + "?unitGroup=metric&include=hours%2Cdays&key=FAKE_API_KEY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(weatherData)))
        );

        // when
        var result = webTestClient
                .get()
                .uri("/forecast/" + city)
                .exchange();

        // then
        result
                .expectBody(WeatherData.class)
                .consumeWith(response -> {
                            assertEquals(weatherData, response.getResponseBody());
                            response.getStatus().isSameCodeAs(HttpStatusCode.valueOf(200));
                        }
                );

    }

}