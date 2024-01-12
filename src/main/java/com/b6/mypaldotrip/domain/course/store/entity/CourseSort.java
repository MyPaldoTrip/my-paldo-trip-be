package com.b6.mypaldotrip.domain.course.store.entity;

import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum CourseSort {
    MODIFIED,
    LEVEL,
    LIKE,
    COMMENT;

    @JsonCreator
    public static CourseSort forValue(String value) {
        for (CourseSort reviewSort : CourseSort.values()) {
            if (reviewSort.name().equalsIgnoreCase(value)) {
                return reviewSort;
            }
        }
        throw new GlobalException(CourseErrorCode.WRONG_COURSE_SORT);
    }
}
