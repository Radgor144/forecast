package com.Forecast.Forecast.weather;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(
        List<String> allowedApi,
        List<String> allowedMethods,
        List<String> allowedHeaders,
        boolean allowCredentials,
        List<String> exposedHeader,
        Long maxAge
) {
}
