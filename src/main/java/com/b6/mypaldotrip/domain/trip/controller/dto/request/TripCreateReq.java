package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TripCreateReq(
        @NotNull(message = "cityName이 비었습니다.") String cityName,
        @NotNull(message = "category가 비었습니다.") Category category,
        @NotBlank(message = "name이 비었습니다.") String name,
        @NotBlank(message = "description이 비었습니다.") String description) {}
