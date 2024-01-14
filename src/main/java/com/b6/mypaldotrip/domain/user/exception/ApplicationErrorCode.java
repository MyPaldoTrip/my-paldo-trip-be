package com.b6.mypaldotrip.domain.user.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ResultCode {
    NOT_FOUND_APPLICATION(HttpStatus.NOT_FOUND, "신청서를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
