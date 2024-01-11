package com.b6.mypaldotrip.domain.user.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ResultCode {
    SEND_MESSAGE_ERROR(HttpStatus.BAD_REQUEST, "MessagingException"),
    VERIFY_TIME_OUT(HttpStatus.NOT_FOUND, "메일 전송 후 일정 시간이 지나 다시 전송해야합니다. "),
    COND_MISMATCH(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
