package com.Forecast.Forecast.cache;

import com.Forecast.Forecast.util.JsonFileReader;
import com.Forecast.Forecast.util.StubUtil;
import com.Forecast.Forecast.weather.WeatherClientCacheConfig;
import com.Forecast.Forecast.weather.data.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;

import static com.Forecast.Forecast.util.RequestUtil.getForecastRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@AutoConfigureWebTestClient
public class CachingIntegrationTest {

    public static final String CITY = "krakow";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private WeatherClientCacheConfig weatherClientCacheConfig;

    @Test
    void getWeatherData_ReturnsFromCache() throws IOException {
        // given
        WeatherData expectedWeatherData = JsonFileReader.readJson(objectMapper, "src/test/resources/" + CITY + ".json", WeatherData.class);
        StubUtil.stubGetWeatherData(objectMapper, CITY, expectedWeatherData);

        // when
        var secondRequestResult = getForecastRequest(webTestClient, CITY);

        // then
        assertEquals(1, WireMock.getAllServeEvents().size());
        secondRequestResult
                .expectStatus().isOk()
                .expectBody(WeatherData.class)
                .isEqualTo(expectedWeatherData);
    }

    @Test
    void getWeatherData_AfterRefreshingCache() throws IOException {

        // given
        WeatherData expectedWeatherData = JsonFileReader.readJson(objectMapper, "src/test/resources/" + CITY + ".json", WeatherData.class);
        StubUtil.stubGetWeatherData(objectMapper, CITY, expectedWeatherData);

        //when
        weatherClientCacheConfig.evictCache();

        var secondRequestResult = getForecastRequest(webTestClient, CITY);

        // then
        assertEquals(2, WireMock.getAllServeEvents().size());
        secondRequestResult
                .expectStatus().isOk()
                .expectBody(WeatherData.class)
                .isEqualTo(expectedWeatherData);
    }
}
