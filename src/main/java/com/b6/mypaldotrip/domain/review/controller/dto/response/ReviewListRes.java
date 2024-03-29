package com.b6.mypaldotrip.domain.review.controller.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReviewListRes(
        Long reviewId,
        String username,
        Long level,
        String content,
        Integer score,
        LocalDateTime modifiedAt) {}
