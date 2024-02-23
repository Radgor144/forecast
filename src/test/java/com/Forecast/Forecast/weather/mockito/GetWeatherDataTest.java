package com.Forecast.Forecast.weather.mockito;

import com.Forecast.Forecast.weather.WeatherService;
import com.Forecast.Forecast.weather.data.WeatherData;
import com.Forecast.Forecast.weather.fixtures.WeatherDataFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GetWeatherDataTest {

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;



    static Stream<Arguments> provideAddressesAndCities() {
        return Stream.of(
                Arguments.of("Kraków, Woj. Małopolskie, Polska", "Krakow"),
                Arguments.of("Szczecin, Woj. Zachodniopomorskie, Polska", "Szczecin"),
                Arguments.of("Poznań, Woj. Wielkopolskie, Polska", "Poznan")
        );
    }

    @ParameterizedTest
    @MethodSource("provideAddressesAndCities")
    public void ServiceTest(String address, String city) {
        // given
        var expectedWeatherData = WeatherDataFixture.defaultWeatherData(address);
        Mockito.when(weatherService.getWeatherData(city)).thenReturn(expectedWeatherData);

        // when
        var result = webTestClient
                .get()
                .uri("/forecast/" + city)
                .exchange();

        // then
        result
                .expectBody(WeatherData.class)
                .consumeWith(response -> {
                    WeatherData returnedWeatherData = response.getResponseBody();
                    assertEquals(expectedWeatherData, returnedWeatherData, "Returned WeatherData should match expected");
                    assertEquals(HttpStatus.OK.value(), response.getStatus().value(), "HTTP status should be OK");

                    // Wypisanie danych
                    System.out.println("Returned WeatherData: " + returnedWeatherData);
                });
    }
}

