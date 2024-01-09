package com.b6.mypaldotrip.domain.course.controller.dto.response;

import lombok.Builder;

@Builder
public record CourseSaveRes(String title, String content) {}
