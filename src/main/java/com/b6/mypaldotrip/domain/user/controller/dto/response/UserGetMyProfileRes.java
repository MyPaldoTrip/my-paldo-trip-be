package com.b6.mypaldotrip.domain.user.controller.dto.response;

import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import lombok.Builder;

@Builder
public record UserGetMyProfileRes(Long userId, String username, UserRole userRole) {}
