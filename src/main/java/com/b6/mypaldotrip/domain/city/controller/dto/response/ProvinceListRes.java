package com.b6.mypaldotrip.domain.city.controller.dto.response;

import lombok.Builder;

@Builder
public record ProvinceListRes(Long cityId, String provinceName) {}
