package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherClient;
import com.Forecast.Forecast.weather.fixtures.WeatherDataFixture;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.Forecast.Forecast.weather.data.WeatherConstants.INCLUDE;
import static com.Forecast.Forecast.weather.data.WeatherConstants.UNIT_GROUP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    private static final String WEATHER_CLIENT_API_KEY = "FAKE_API_KEY_CLIENT";

    @Mock
    private WeatherClient weatherClient;

    @ParameterizedTest
    @MethodSource("shouldReturnWeatherDataForGivenCityMethodSource")
    public void shouldReturnWeatherDataForGivenCity(String address, String city) {
        // given
        var expectedWeatherData = WeatherDataFixture.defaultWeatherData(address);
        WeatherService weatherService = new WeatherService(weatherClient, WEATHER_CLIENT_API_KEY);

        when(weatherClient.getWeatherData(UNIT_GROUP, INCLUDE, WEATHER_CLIENT_API_KEY, city)).thenReturn(expectedWeatherData);

        // when
        var result = weatherService.getWeatherData(city);

        // then
        assertEquals(expectedWeatherData, result);
    }

    static Stream<Arguments> shouldReturnWeatherDataForGivenCityMethodSource() {
        return Stream.of(
                Arguments.of("Kraków, Woj. Małopolskie, Polska", "Krakow"),
                Arguments.of("Szczecin, Woj. Zachodniopomorskie, Polska", "Szczecin"),
                Arguments.of("Poznań, Woj. Wielkopolskie, Polska", "Poznan")
        );
    }
}