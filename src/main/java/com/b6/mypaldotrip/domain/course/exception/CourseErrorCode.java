package com.b6.mypaldotrip.domain.course.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseErrorCode implements ResultCode {
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "선택하신 코스가 존재하지 않습니다"),
    WRONG_COURSE_SORT(HttpStatus.BAD_REQUEST, "잘못된 코스 정렬 방식 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
