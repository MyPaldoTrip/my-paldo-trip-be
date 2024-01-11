package com.b6.mypaldotrip.domain.user.controller.dto.request;

import jakarta.validation.constraints.Email;

public record EmailSendReq(@Email(message = "이메일 형식이 아닙니다.") String email) {}
