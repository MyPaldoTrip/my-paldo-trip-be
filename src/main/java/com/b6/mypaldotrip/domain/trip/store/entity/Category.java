package com.b6.mypaldotrip.domain.trip.store.entity;

import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    ATTRACTION,
    RESTAURANT,
    FESTIVAL;

    @JsonCreator
    public static Category forValue(String value) {
        for (Category category : Category.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new GlobalException(TripErrorCode.WRONG_CATEGORY_ERROR);
    }
}
