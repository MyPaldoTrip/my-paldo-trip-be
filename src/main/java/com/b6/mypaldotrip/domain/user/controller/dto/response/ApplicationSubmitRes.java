package com.b6.mypaldotrip.domain.user.controller.dto.response;

import lombok.Builder;

@Builder
public record ApplicationSubmitRes(String email, String username, String title, String content) {}
