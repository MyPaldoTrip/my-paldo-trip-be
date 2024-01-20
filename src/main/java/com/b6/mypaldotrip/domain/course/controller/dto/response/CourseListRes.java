package com.b6.mypaldotrip.domain.course.controller.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CourseListRes(
        Long courseId,
        String username,
        String title,
        String content,
        int totalPage,
        LocalDateTime createdAt,
        Long level,
        int commentCount,
        int likeCount) {}
