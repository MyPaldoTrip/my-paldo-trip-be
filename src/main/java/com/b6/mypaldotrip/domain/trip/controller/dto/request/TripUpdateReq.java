package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;

// TODO: 2024-01-08 cityId 추가 필요
public record TripUpdateReq(Category category, String name, String description) {}
