package com.b6.mypaldotrip.domain.trip.controller.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record TripListWrapper(List<TripListRes> tripListRes) {}
