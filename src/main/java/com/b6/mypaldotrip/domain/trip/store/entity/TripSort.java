package com.b6.mypaldotrip.domain.trip.store.entity;

import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum TripSort {
    CREATED,
    RATING,
    REVIEWS;

    @JsonCreator
    public static TripSort forValue(String value) {
        for (TripSort tripSort : TripSort.values()) {
            if (tripSort.name().equalsIgnoreCase(value)) {
                return tripSort;
            }
        }
        throw new GlobalException(TripErrorCode.WRONG_TRIP_SORT);
    }
}
