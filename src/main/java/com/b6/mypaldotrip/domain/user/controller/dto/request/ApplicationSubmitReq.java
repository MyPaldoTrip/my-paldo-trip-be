package com.b6.mypaldotrip.domain.user.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ApplicationSubmitReq(
        @NotBlank(message = "제목을 입력하세요") String title,
        @NotBlank(message = "내용을 입력하세요") String content) {}
