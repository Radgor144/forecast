package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherClient;
import com.Forecast.Forecast.weather.data.WeatherData;
import com.Forecast.Forecast.weather.fixtures.WeatherDataFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.Forecast.Forecast.util.RequestUtil.getForecastRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class ForecastControllerMockitoTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private WeatherClient weatherClient;

    @Autowired
    private WeatherService weatherService;

    @Test
    void happyPathWithMockito() {
//      given
        var weatherData = WeatherDataFixture.defaultWeatherData("Kraków, Woj. Małopolskie, Polska");
//      when
        var result = getForecastRequest(webTestClient, "Kraków");

        when(weatherClient.getWeatherData(any(), any(), any(), any())).thenReturn(weatherData);
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
