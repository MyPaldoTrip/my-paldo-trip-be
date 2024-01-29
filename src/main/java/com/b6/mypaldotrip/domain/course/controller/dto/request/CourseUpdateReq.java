package com.b6.mypaldotrip.domain.course.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CourseUpdateReq(
        @NotBlank(message = "title 이 비었습니다.") String title,
        @NotBlank(message = "content 가 비었습니다.") String content,
        @NotBlank(message = "cityName 이 비었습니다.") String cityName,
        List<Long> tripIds) {}
