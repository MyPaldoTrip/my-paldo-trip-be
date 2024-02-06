package com.b6.mypaldotrip.domain.user.controller.dto.request;

import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ApplicationCheckReq(
        @NotNull(message = "신청서 id는 null이 될 수 없습니다.") Long applicationId,
        @Nullable String accept) {}
