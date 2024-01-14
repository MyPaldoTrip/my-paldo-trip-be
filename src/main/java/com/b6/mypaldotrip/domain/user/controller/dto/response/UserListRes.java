package com.b6.mypaldotrip.domain.user.controller.dto.response;

import lombok.Builder;

@Builder
public record UserListRes(
        Long userId,
        String email,
        String username,
        Long age,
        Long level,
        String userRoleValue,
        int writeReviewCnt,
        int followerCnt) {}
