package com.b6.mypaldotrip.domain.city.controller.dto.response;

import lombok.Builder;

@Builder
public record CityCreateRes(
    String provinceName,
    String cityName,
    String cityInfo
) {

}
