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
    BEFORE_EMAIL_VALIDATION(HttpStatus.PRECONDITION_FAILED, "해당 이메일은 인증코드를 받아 검증 받지 않은 상태입니다."),
    WRONG_USER_SORT(HttpStatus.BAD_REQUEST, "잘못된 유저 정렬 방식 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
