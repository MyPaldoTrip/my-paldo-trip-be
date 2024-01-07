package com.b6.mypaldotrip.global.template;

import org.springframework.http.HttpStatus;

public interface ResultCode {

    String name();

    HttpStatus getHttpStatus();

    String getMessage();
}
