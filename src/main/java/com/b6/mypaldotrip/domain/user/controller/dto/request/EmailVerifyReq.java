package com.b6.mypaldotrip.domain.user.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerifyReq(
        @Email(message = "이메일 형식이 아닙니다.") String email,
        @NotBlank(message = "인증 코드가 비었습니다.") String code) {}
