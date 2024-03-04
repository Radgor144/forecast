package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;

    // da sie znalezc miasto po kodzie pocztowym!
    @GetMapping("/forecast/{city}")
//   TODO: ogarnąć, żeby ten validator działał na poziomie controllera pusty string podany w paramaetrze wywowałania z postamana
//powinien byc odrzucany na wyższym poziomie
    public WeatherData getWeatherData(@PathVariable @Valid @NotBlank String city) {
            return weatherService.getWeatherData(city);
        }
}