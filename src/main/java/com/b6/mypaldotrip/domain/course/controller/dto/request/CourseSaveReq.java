package com.b6.mypaldotrip.domain.course.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CourseSaveReq(
        @NotBlank(message = "title이 비었습니다.") String title,
        @NotBlank(message = "content가 비었습니다.") String content,
        @NotBlank(message = "cityId가 비었습니다.") String cityName) {}
