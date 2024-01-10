package com.b6.mypaldotrip.domain.trip.controller.dto.response;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import lombok.Builder;

@Builder
public record TripCreateRes(String city, Category category, String name, String description) {}
