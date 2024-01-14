package com.b6.mypaldotrip.domain.user.controller.dto.response;

import lombok.Builder;

@Builder
public record ApplicationConfirmRes(Long applicationId, String email, String message) {}
