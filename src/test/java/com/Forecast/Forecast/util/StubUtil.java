package com.Forecast.Forecast.util;

import com.Forecast.Forecast.weather.data.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@UtilityClass
public final class StubUtil {

    public static void stubGetWeatherData(ObjectMapper objectMapper, String city, WeatherData weatherData) throws JsonProcessingException {
        stubFor(get(urlEqualTo("/VisualCrossingWebServices/rest/services/timeline/" + city + "?unitGroup=metric&include=hours%2Cdays&key=FAKE_API_KEY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(weatherData)))
        );

    }
}
