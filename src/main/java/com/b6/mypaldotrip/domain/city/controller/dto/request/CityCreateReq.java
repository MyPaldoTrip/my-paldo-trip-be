package com.b6.mypaldotrip.domain.city.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CityCreateReq(
    @NotBlank(message = "도시명이 비었습니다.")
    String cityName,
    @NotBlank(message = "도시정보가 비었습니다.")
    String cityInfo
    ) {

}
