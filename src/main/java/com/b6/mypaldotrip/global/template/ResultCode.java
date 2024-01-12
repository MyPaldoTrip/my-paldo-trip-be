package com.b6.mypaldotrip.global.template;

import org.springframework.http.HttpStatus;

public interface ResultCode {
    HttpStatus getHttpStatus();

    String getMessage();
}
