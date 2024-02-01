package com.b6.mypaldotrip.domain.weather.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum WeatherErrorCode implements ResultCode {
    WEATHER_CHECK_FAIL(HttpStatus.BAD_REQUEST, "날씨 조회 실패.");
    private final HttpStatus httpStatus;
    private final String message;
}
