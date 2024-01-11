package com.b6.mypaldotrip.domain.review.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ResultCode {
    NON_EXIST_REVIEW(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
