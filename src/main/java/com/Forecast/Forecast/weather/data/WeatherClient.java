package com.Forecast.Forecast.weather.data;

import com.Forecast.Forecast.weather.data.UnitGroup;
import com.Forecast.Forecast.weather.data.WeatherData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weather-client")
public interface WeatherClient {

    @GetMapping("/VisualCrossingWebServices/rest/services/timeline/{city}")
    WeatherData getWeatherData(
            @RequestParam("unitGroup") UnitGroup unitGroup,
            @RequestParam("include") String include,
            @RequestParam("key") String key,
            @RequestParam("city") String city
    );
}
