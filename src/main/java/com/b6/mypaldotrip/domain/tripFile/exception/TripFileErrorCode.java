package com.b6.mypaldotrip.domain.tripFile.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TripFileErrorCode implements ResultCode {
    NON_EXIST_FILE(HttpStatus.NOT_FOUND, "해당 파일이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
