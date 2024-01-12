package com.b6.mypaldotrip.domain.user.controller.dto.response;

import lombok.Builder;

@Builder
public record EmailSendRes(String recipientEmail, String message) {}
