package com.b6.mypaldotrip.domain.review.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ReviewUpdateReq(
        String content,
        @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
                @Max(value = 5, message = "평점은 최대 5점이어야 합니다.")
                Integer score) {}
