package com.b6.mypaldotrip.domain.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserSignUpReq(
        @NotBlank(message = "email이 빈 값입니다") String email,
        @NotBlank(message = "password가 빈 값입니다") String password,
        @NotBlank(message = "username이 빈 값입니다") String username) {}
