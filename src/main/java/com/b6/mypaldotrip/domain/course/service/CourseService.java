package com.b6.mypaldotrip.domain.course.service;

import com.b6.mypaldotrip.domain.course.controller.dto.requeset.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseSaveRes saveCourse(CourseSaveReq req) {
        CourseEntity course = CourseEntity.builder()
            .title(req.title())
            .content(req.content())
            .build();

        courseRepository.save(course);

        CourseSaveRes res = CourseSaveRes.builder()
            .title(course.getTitle())
            .content(course.getContent())
            .build();

        return res;
    }
}
