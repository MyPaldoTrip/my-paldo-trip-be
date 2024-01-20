package com.b6.mypaldotrip.domain.courseFile.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CourseFileErrorCode implements ResultCode {
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "선택하신 파일이 존재하지 않습니다"),
    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
