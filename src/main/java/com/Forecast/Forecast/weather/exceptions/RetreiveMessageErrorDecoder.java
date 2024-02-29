package com.Forecast.Forecast.weather.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetreiveMessageErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("Error Decoder: {}, {}", methodKey, response);

        switch (response.status()) {
            case 400:
            case 404:
                return new WeatherDataNotFoundException("Weather data not found");
            case 408:
            case 503:
                return new BadGatewayException("Bad Gateway");
            default:
                return new UnknownException();
        }
    }


}

