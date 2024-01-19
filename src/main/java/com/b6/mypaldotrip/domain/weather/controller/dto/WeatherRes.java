package com.b6.mypaldotrip.domain.weather.controller.dto;

public record WeatherRes(
    String baseDate,//날짜
    String baseTime, //시간
    String category,//예보
    String fcstValue)// 예보 상세값)

{
}
