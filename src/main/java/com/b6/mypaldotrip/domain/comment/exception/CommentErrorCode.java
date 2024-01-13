package com.b6.mypaldotrip.domain.comment.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ResultCode {
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),
    WRONG_COMMENT_SORT(HttpStatus.BAD_REQUEST, "잘못된 댓글 정렬 방식 입니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글이 존재하지 않습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
