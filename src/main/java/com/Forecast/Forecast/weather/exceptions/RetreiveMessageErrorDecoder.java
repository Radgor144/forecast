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

        if(methodKey.contains("getWeatherData")){
            return new WeatherDataNotFoundException("Weather data not found");
        }
        return new UnknownException();
    }
}

