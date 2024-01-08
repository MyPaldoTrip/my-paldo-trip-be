package com.b6.mypaldotrip.domain.trip.controller.dto.response;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import lombok.Builder;

// TODO: 2024-01-08 city명 추가 필요
@Builder
public record TripCreateRes(Category category,
                            String name,
                            String description) {
}
