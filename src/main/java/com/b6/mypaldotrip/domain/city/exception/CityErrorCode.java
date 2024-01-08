package com.b6.mypaldotrip.domain.city.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CityErrorCode implements ResultCode {

    ALREADY_EXIST_CITY(HttpStatus.CONFLICT, "이미 존재하는 도시명입니다.");
    private final HttpStatus httpStatus;

    private final String message;

}
