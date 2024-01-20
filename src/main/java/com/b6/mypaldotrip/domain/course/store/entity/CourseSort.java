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
        for (CourseSort courseSort : CourseSort.values()) {
            if (courseSort.name().equalsIgnoreCase(value)) {
                return courseSort;
            }
        }
        throw new GlobalException(CourseErrorCode.WRONG_COURSE_SORT);
    }
}
