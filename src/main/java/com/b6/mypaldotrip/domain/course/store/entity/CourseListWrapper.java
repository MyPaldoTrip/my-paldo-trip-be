package com.b6.mypaldotrip.domain.course.store.entity;

import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseListRes;
import java.util.List;
import lombok.Builder;

@Builder
public record CourseListWrapper(List<CourseListRes> courseListResList) {}
