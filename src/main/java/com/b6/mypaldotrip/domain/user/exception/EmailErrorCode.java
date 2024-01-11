package com.b6.mypaldotrip.domain.user.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ResultCode {
    SEND_MESSAGE_ERROR(HttpStatus.BAD_REQUEST, "MessagingException");

    private final HttpStatus httpStatus;
    private final String message;
}
