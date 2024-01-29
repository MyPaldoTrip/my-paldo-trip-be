package com.b6.mypaldotrip.domain.follow.controller.dto.response;

import lombok.Builder;

@Builder
public record FollowRes(Long userId, String username, String email) {}
