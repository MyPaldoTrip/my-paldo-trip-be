package com.b6.mypaldotrip.domain.chat.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements ResultCode {
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방이 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
