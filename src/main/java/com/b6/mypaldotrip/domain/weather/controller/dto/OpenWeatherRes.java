package com.b6.mypaldotrip.domain.weather.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenWeatherRes(@JsonProperty("list") List<WeatherItem> list) {

    public record WeatherItem(
            @JsonProperty("main") Main main,
            @JsonProperty("weather") List<Weather> weather,
            @JsonProperty("dt_txt") String date) {}

    public record Main(
            @JsonProperty("temp_min") double temp_min, @JsonProperty("temp_max") double temp_max) {}

    public record Weather(@JsonProperty("main") String main) {}
}
