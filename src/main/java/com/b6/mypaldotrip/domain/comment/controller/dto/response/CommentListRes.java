package com.b6.mypaldotrip.domain.comment.controller.dto.response;

import lombok.Builder;

@Builder
public record CommentListRes(Long commentId, String username, String content, int totalPage) {}
