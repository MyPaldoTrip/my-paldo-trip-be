package com.b6.mypaldotrip.domain.course.controller.dto.request;

import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;

public record CourseSearchReq(String username, String cityName, CourseSort courseSort) {}