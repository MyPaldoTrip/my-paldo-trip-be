package com.b6.mypaldotrip.global.exception;

import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final VersionConfig versionConfig;

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<RestResponse<Object>> handleException(GlobalException e) {

        return RestResponse.error(e.getResultCode(), versionConfig.getVersion()).toResponseEntity();
    }
}
