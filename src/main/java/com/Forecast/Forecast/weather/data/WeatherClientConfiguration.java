package com.Forecast.Forecast.weather.data;

import com.Forecast.Forecast.weather.exceptions.WeatherClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

class WeatherClientConfiguration {
    @Bean
    ErrorDecoder errorDecoder() {
        return new WeatherClientErrorDecoder();
    }
}

