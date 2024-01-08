package com.b6.mypaldotrip.domain.sample.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SampleErrorCode implements ResultCode {
    NO_TITLE_ERROR(HttpStatus.BAD_REQUEST, "제목이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
