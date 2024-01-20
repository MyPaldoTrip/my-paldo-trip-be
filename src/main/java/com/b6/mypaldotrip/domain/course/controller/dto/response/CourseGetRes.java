package com.b6.mypaldotrip.domain.course.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record CourseGetRes(
        Long courseId,
        String username,
        String title,
        String content,
        List<String> fileURL,
        LocalDateTime createdAt) {}
