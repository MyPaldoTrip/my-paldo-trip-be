package com.b6.mypaldotrip.domain.trip.controller.dto.response;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import java.util.List;
import lombok.Builder;

@Builder
public record TripGetRes(
        Long tripId,
        String city,
        Category category,
        String name,
        String description,
        List<String> urlList) {}
