package com.b6.mypaldotrip.domain.follow.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ResultCode {
    SAME_USER_ID(HttpStatus.BAD_REQUEST, "자신은 팔로우 할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
