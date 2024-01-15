package com.b6.mypaldotrip.domain.city.store.entity;

import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum CitySort {
    INITIAL, // 초기 설정
    COUNT; // 여행정보가 많은 순

    @JsonCreator
    public static CitySort forValue(String value) {
        for (CitySort citySort : CitySort.values()) {
            if (citySort.name().equalsIgnoreCase(value)) {
                return citySort;
            }
        }
        throw new GlobalException(CityErrorCode.WRONG_CITY_SORT);
    }
}
