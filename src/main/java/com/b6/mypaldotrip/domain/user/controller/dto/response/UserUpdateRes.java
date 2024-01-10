package com.b6.mypaldotrip.domain.user.controller.dto.response;

import lombok.Builder;

@Builder
public record UserUpdateRes(
        String email,
        String username,
        String introduction,
        String profileURL,
        Long age,
        Long level) {}
