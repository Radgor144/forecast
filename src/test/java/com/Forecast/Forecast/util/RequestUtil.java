package com.Forecast.Forecast.util;

import lombok.experimental.UtilityClass;
import org.springframework.test.web.reactive.server.WebTestClient;

@UtilityClass
public class RequestUtil {
    public static WebTestClient.ResponseSpec getForecastRequest(WebTestClient webTestClient, String city) {
        return webTestClient
                .get()
                .uri("/forecast/" + city)
                .exchange();
    }
}
