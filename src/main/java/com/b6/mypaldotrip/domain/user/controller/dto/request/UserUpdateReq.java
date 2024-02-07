package com.b6.mypaldotrip.domain.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record UserUpdateReq(
        @NotBlank(message = "username이 빈 값입니다") String username,
        String introduction,
        Long age,
        @Length(max = 20) @NotBlank(message = "password가 빈 값입니다") String password) {}
