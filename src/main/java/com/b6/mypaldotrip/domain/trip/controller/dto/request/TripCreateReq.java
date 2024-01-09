package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// TODO: 2024-01-08 cityId 추가 필요
public record TripCreateReq(
        @NotNull(message = "category가 비었습니다.") Category category,
        @NotBlank(message = "name이 비었습니다.") String name,
        @NotBlank(message = "description이 비었습니다.") String description) {}
