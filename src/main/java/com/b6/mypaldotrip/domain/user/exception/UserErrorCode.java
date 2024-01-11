package com.b6.mypaldotrip.domain.user.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ResultCode {
    NOT_FOUND_USER_BY_EMAIL(HttpStatus.BAD_REQUEST, "해당 이메일로 가입된 유저는 없습니다."),
    NOT_FOUND_USER_BY_USERID(HttpStatus.BAD_REQUEST, "해당 id에 해당하는 유저는 없습니다."),
    VERIFIED_TIME_OUT(HttpStatus.REQUEST_TIMEOUT, "이메일 인증을 하지 않았거나, 이메일 검증 유효 시간이 지났습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
