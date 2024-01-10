package com.b6.mypaldotrip.domain.comment.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record CommentSaveReq(@NotNull(message = "content가 비었습니다.") String content) {}
