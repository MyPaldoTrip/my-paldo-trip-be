package com.b6.mypaldotrip.domain.user.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record ApplicationCheckReq(
        @NotNull(message = "신청서 id는 null이 될 수 없습니다.") Long applicationId, String accept) {}
