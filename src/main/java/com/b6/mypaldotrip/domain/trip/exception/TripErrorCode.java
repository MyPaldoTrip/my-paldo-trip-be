package com.b6.mypaldotrip.domain.trip.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TripErrorCode implements ResultCode {
    WRONG_CATEGORY_ERROR(HttpStatus.BAD_REQUEST, "카테고리가 없거나 잘못된 카테고리 입니다."),
    ALREADY_EXIST_TRIP(HttpStatus.BAD_REQUEST, "이미 존재하는 여행정보 입니다."),
    NON_EXIST_TRIP(HttpStatus.NOT_FOUND, "해당 여행정보가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
