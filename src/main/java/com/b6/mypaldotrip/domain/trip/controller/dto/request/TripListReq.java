package com.b6.mypaldotrip.domain.trip.controller.dto.request;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;

public record TripListReq(String cityName, Category category, int page) {}
