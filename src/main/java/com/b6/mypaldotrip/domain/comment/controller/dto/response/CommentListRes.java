package com.b6.mypaldotrip.domain.comment.controller.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CommentListRes(
        Long commentId,
        String username,
        String content,
        LocalDateTime modifiedAt,
        Long level,
        int totalPage) {}
