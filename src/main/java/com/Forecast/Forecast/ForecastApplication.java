package com.Forecast.Forecast;

import com.Forecast.Forecast.weather.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableFeignClients
@EnableWebMvc
@EnableConfigurationProperties(CorsProperties.class)
public class ForecastApplication {
	public static void main(String[] args) {
		SpringApplication.run(ForecastApplication.class, args);
	}
}
