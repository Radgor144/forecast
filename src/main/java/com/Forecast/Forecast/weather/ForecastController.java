package com.Forecast.Forecast.weather;

import com.Forecast.Forecast.weather.data.WeatherData;
import com.Forecast.Forecast.weather.exceptions.WeatherDataNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final WeatherService weatherService;

    @GetMapping("/forecast/{city}")
//   TODO: ogarnąć, żeby ten validator działał na poziomie controllera pusty string podany w paramaetrze wywowałania z postamana
//powinien byc odrzucany na wyższym poziomie
    public WeatherData getWeatherData(@PathVariable @Valid @NotBlank String city) {
        try {
            return weatherService.getWeatherData(city);
        } catch (WeatherDataNotFoundException ex) {
//          TODO: to powinno być ogarnięte restcontrolleradvice
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Weather data not found", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex);
        }
    }
}