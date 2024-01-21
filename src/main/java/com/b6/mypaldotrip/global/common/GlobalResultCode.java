package com.b6.mypaldotrip.global.common;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalResultCode implements ResultCode {
    SUCCESS(HttpStatus.OK, "정상 처리 되었습니다"),
    CREATED(HttpStatus.CREATED, "저장 되었습니다."),
    DUPLICATE(HttpStatus.CONFLICT, "중복 에러입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation에러입니다. 입력 조건을 확인해주세요"),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.FORBIDDEN, "토큰이 존재하지 않거나 유효하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
