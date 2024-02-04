package com.Forecast.Forecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ForecastApplication implements CommandLineRunner {

	@Autowired
	private WeatherClient weatherClient;

	public static void main(String[] args) {
		SpringApplication.run(ForecastApplication.class, args);

	}

	@Override
	public void run(String... args) {
		String include = "hours,days";
		String key = "AM9ZTRLEYSXEF4YT5E79H7GBJ";
		String city = "Krakow";
		System.out.println("xyz");
		WeatherData weatherData = weatherClient.getWeatherData(include, key, city);
		System.out.println(weatherData);

	}
}
