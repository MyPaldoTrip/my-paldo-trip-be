package com.b6.mypaldotrip.domain.weather.controller.dto;

public record WeatherReq(
    String serviceKey,
    String pageNo,
    String numOfRows,
    String base_date,
    String base_time,
    String dataType, //json
    String nx, //x좌표
    String ny// y 좌표
) {

}
