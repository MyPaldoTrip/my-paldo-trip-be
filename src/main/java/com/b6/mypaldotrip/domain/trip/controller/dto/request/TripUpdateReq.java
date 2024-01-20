package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;

public record TripUpdateReq(Long cityId, Category category, String name, String description) {}
