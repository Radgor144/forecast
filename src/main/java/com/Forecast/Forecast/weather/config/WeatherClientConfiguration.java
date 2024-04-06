package com.Forecast.Forecast.weather.config;

import com.Forecast.Forecast.weather.exceptions.WeatherClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class WeatherClientConfiguration {
    @Bean
    ErrorDecoder errorDecoder() {
        return new WeatherClientErrorDecoder();
    }
}

