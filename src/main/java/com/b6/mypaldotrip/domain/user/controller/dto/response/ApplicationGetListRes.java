package com.b6.mypaldotrip.domain.user.controller.dto.response;

import lombok.Builder;

@Builder
public record ApplicationGetListRes(
        Long applicationId, String email, String username, String title, Boolean verified) {}
