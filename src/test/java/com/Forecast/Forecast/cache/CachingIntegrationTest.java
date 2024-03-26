package com.Forecast.Forecast.cache;

import com.Forecast.Forecast.util.JsonFileReader;
import com.Forecast.Forecast.util.StubUtil;
import com.Forecast.Forecast.weather.data.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
public class CachingIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    CacheManager manager;

    @Test
    void getWeatherData_ReturnsFromCache() throws IOException {
        // given
        String city = "krakow";
        WeatherData expectedWeatherData = JsonFileReader.readJson(objectMapper, "src/test/resources/krakow.json", WeatherData.class);

        StubUtil.stubGetWeatherData(objectMapper, city, expectedWeatherData);

        // when
        var result = webTestClient
                .get()
                .uri("/forecast/" + city)
                .exchange();

        Cache cache = manager.getCache("WeatherData");
        WeatherData cachedWeatherData = (WeatherData) cache.get(city).get();

        // then

        assertEquals(expectedWeatherData, cachedWeatherData);

        result
                .expectBody(WeatherData.class)
                .consumeWith(response -> {
                            assertEquals(cachedWeatherData, response.getResponseBody());
                            response.getStatus().isSameCodeAs(HttpStatusCode.valueOf(200));
                        }
                );
    }
}
