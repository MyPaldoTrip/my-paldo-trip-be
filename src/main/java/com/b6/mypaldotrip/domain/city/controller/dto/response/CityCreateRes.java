package com.b6.mypaldotrip.domain.city.controller.dto.response;

import lombok.Builder;

@Builder
public record CityCreateRes(
    String cityName,
    String cityInfo
) {

}
