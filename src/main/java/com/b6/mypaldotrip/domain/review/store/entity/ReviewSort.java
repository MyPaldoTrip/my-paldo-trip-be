package com.b6.mypaldotrip.domain.review.store.entity;

import com.b6.mypaldotrip.domain.review.exception.ReviewErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReviewSort {
    MODIFIED,
    RATING,
    LEVEL,
    FOLLOW;

    @JsonCreator
    public static ReviewSort forValue(String value) {
        for (ReviewSort reviewSort : ReviewSort.values()) {
            if (reviewSort.name().equalsIgnoreCase(value)) {
                return reviewSort;
            }
        }
        throw new GlobalException(ReviewErrorCode.WRONG_REVIEW_SORT);
    }
}
