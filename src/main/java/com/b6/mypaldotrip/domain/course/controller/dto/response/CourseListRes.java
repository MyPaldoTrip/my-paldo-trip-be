package com.b6.mypaldotrip.domain.course.controller.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CourseListRes(
        Long courseId,
        String username,
        String title,
        String content,
        int totalPage,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
                @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                LocalDateTime createdAt,
        Long level,
        int commentCount,
        int likeCount,
        String thumbnailUrl) {}
