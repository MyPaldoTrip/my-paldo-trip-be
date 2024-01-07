package com.b6.mypaldotrip.global.exception;

import com.b6.mypaldotrip.global.template.ResultCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private ResultCode resultCode;

    public GlobalException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
