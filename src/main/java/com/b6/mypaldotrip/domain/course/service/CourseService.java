package com.b6.mypaldotrip.domain.course.service;

import com.b6.mypaldotrip.domain.course.controller.dto.requeset.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseGetRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseListRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import com.b6.mypaldotrip.domain.sample.exception.SampleErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.List;
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

    public List<CourseListRes> getCourseList() {
        List<CourseListRes> res = courseRepository.findAll().stream()
            .map(c -> CourseListRes.builder()
                .title(c.getTitle())
                .content(c.getContent())
                .build()
            ).toList();

        return res;
    }

    public CourseGetRes getCourse(Long courseId) {
        CourseEntity course = findCourse(courseId);
        CourseGetRes res = CourseGetRes.builder()
            .title(course.getTitle())
            .content(course.getContent())
            .build();

        return res;
    }

    private CourseEntity findCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(
            () -> new GlobalException(CourseErrorCode.COURSE_NOT_FOUND)
        );
    }
}
