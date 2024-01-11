package com.b6.mypaldotrip.domain.review.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ResultCode {
    NON_EXIST_REVIEW(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다."),
    MISMATCHED_TRIP_REVIEW(HttpStatus.BAD_REQUEST, "해당 리뷰가 등록된 여행정보가 아닙니다."),
    NOT_AUTHOR_ERROR(HttpStatus.BAD_REQUEST, "해당 리뷰의 작성자만 수정 및 삭제 가능합니다."),
    WRONG_REVIEW_SORT(HttpStatus.BAD_REQUEST, "잘못된 후기 정렬 방식 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
