package com.b6.mypaldotrip.domain.city.controller.dto.request;

import lombok.Builder;

@Builder
public record CityUpdateReq(String provinceName, String cityName, String cityInfo) {}
