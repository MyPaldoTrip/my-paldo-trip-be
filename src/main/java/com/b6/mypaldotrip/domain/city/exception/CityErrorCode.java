package com.b6.mypaldotrip.domain.city.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CityErrorCode implements ResultCode {

    CITY_NOT_FOUND(HttpStatus.NOT_FOUND, "선택하신 시 가 존재하지 않습니다"),

    ALREADY_CITY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 시 입니다.");
    private final HttpStatus httpStatus;

    private final String message;

}
