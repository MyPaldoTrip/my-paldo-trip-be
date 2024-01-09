package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import jakarta.validation.constraints.NotBlank;

// TODO: 2024-01-08 cityId 추가 필요
public record TripCreateReq(
        Category category,
        @NotBlank(message = "name이 비었습니다.") String name,
        @NotBlank(message = "description이 비었습니다.") String description) {}
