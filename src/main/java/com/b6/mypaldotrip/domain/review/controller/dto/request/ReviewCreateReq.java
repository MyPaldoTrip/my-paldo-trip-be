package com.b6.mypaldotrip.domain.review.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateReq(
        @NotBlank(message = "content가 비었습니다.") String content,
        @NotNull(message = "평점을 반드시 입력해야 합니다.")
                @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
                @Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
                Integer score) {}
