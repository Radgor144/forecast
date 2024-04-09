package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherClient;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.springtest.MockServerTest;
import org.mockserver.verify.VerificationTimes;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

import static com.Forecast.Forecast.weather.data.WeatherConstants.INCLUDE;
import static com.Forecast.Forecast.weather.data.WeatherConstants.UNIT_GROUP;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(classes = {WeatherClient.class})
@EnableFeignClients(clients = WeatherClient.class)
@ImportAutoConfiguration({FeignAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class})
@MockServerTest({"spring.cloud.openfeign.client.config.weather-client.url=http://localhost:${mockServerPort}"})
public class RetryerConfigTest{

    public static final String URL = "/VisualCrossingWebServices/rest/services/timeline/XXX";

    @SpyBean
    private WeatherClient weatherClient;
    private MockServerClient mockServerClient;

    @Test
    void testRetryerAttempts(){
        HttpRequest requestDefinition = request()
                .withMethod("GET")
                .withPath(URL);


        mockServerClient
                .when(requestDefinition)
                .respond(response()
                        .withHeader("Retry-After", "1")
                        .withStatusCode(HttpStatusCode.SERVICE_UNAVAILABLE_503.code())
                        .withContentType(org.mockserver.model.MediaType.APPLICATION_JSON));
        try{
            weatherClient.getWeatherData(UNIT_GROUP, INCLUDE, "FAKE_API_KEY","XXX");
        }catch(final Exception e){
            System.out.println("xddd" + e);
        }finally{
            mockServerClient.verify(requestDefinition, VerificationTimes.exactly(5));
        }
    }
}
