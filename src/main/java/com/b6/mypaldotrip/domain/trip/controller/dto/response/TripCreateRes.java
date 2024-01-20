package com.b6.mypaldotrip.domain.trip.controller.dto.response;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import java.util.List;
import lombok.Builder;

@Builder
public record TripCreateRes(
        String city,
        Category category,
        String name,
        String description,
        List<TripFileEntity> tripFileList) {}
