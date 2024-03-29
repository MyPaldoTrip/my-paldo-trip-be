package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import lombok.Builder;

@Builder
public record TripUpdateReq(String cityName, Category category, String name, String description) {}
