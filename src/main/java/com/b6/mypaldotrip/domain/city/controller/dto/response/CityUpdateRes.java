package com.b6.mypaldotrip.domain.city.controller.dto.response;

import lombok.Builder;

@Builder
public record CityUpdateRes(String provinceName, String cityName, String cityInfo) {}
