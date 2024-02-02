package com.b6.mypaldotrip.domain.course.controller.dto.request;

import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import lombok.Builder;

@Builder
public record CourseSearchReq(
        String filterByCityName, Boolean filterByFollowing, CourseSort courseSort) {}
