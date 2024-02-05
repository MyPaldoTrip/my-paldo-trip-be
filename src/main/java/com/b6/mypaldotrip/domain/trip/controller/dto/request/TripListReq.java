package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.trip.store.entity.TripSort;
import lombok.Builder;

@Builder
public record TripListReq(
        String cityName, Category category, TripSort tripSort, int page, int size) {}
