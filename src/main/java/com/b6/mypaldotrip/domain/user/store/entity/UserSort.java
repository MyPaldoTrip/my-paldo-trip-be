package com.b6.mypaldotrip.domain.user.store.entity;

import com.b6.mypaldotrip.domain.user.exception.UserErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserSort {
    MODIFIED,
    AGE,
    LEVEL,
    WRITE_REVIEW_CNT,
    FOLLOWER_CNT;

    @JsonCreator
    public static UserSort forValue(String value) {
        for (UserSort userSort : UserSort.values()) {
            if (userSort.name().equalsIgnoreCase(value)) {
                return userSort;
            }
        }
        throw new GlobalException(UserErrorCode.WRONG_USER_SORT);
    }
}
