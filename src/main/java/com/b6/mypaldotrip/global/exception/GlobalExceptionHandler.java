package com.b6.mypaldotrip.global.exception;

import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.template.ResultCode;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ArrayList<String> errors = new ArrayList<>();
        e.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        ResultCode resultCode = GlobalResultCode.VALIDATION_ERROR;
        return RestResponse.argumentValidException(errors, versionConfig.getVersion())
                .toResponseEntity();
    }
}