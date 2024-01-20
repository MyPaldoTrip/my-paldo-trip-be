package com.b6.mypaldotrip.domain.review.controller.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewListRes(
        Long reviewId, String username, Long level, String content, Integer score, LocalDateTime modifiedAt) {}
