package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest(properties =
        "spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${wiremock.server.port}",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@Slf4j
@RunWith(SpringRunner.class)
public class ErrorTest {

    public static final String LINK = "/VisualCrossingWebServices/rest/services/timeline/krakow?unitGroup=metric&include=hours%2Cdays&key=FAKE_API_KEY";
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void get404Error() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        var errorResponse = new ErrorResponse("Weather data not found", "City not found", HttpStatus.NOT_FOUND.value());
        log.info("Error Decoder: {}", objectMapper.writeValueAsString(errorResponse));
        stubFor(get(urlEqualTo(LINK))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(errorResponse)))
                );

        //when
        var result = webTestClient
                .get()
                .uri("/forecast/krakow")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class);


        //Then
        result
                .isEqualTo(errorResponse);
    }

    @Test
    public void get503Error() throws Exception {
        // given
        ObjectMapper objectMapper = new ObjectMapper();
        var errorResponse = new ErrorResponse("Bad Gateway", "City not found", HttpStatus.SERVICE_UNAVAILABLE.value());
        log.info("Error Decoder: {}", objectMapper.writeValueAsString(errorResponse));
        stubFor(get(urlEqualTo(LINK))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(errorResponse)))
        );

        // when & then
        webTestClient
                .get()
                .uri("/forecast/krakow")
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
