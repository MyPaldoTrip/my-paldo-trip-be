package com.b6.mypaldotrip.domain.course.service;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseUpdateReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseDeleteRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseGetRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseListRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseUpdateRes;
import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseSaveRes saveCourse(CourseSaveReq req) {
        CourseEntity course =
                CourseEntity.builder().title(req.title()).content(req.content()).build();

        courseRepository.save(course);

        CourseSaveRes res =
                CourseSaveRes.builder()
                        .title(course.getTitle())
                        .content(course.getContent())
                        .build();

        return res;
    }

    public List<CourseListRes> getCourseList() {
        List<CourseListRes> res =
                courseRepository.findAll().stream()
                        .map(
                                c ->
                                        CourseListRes.builder()
                                                .title(c.getTitle())
                                                .content(c.getContent())
                                                .build())
                        .toList();

        return res;
    }

    public CourseGetRes getCourse(Long courseId) {
        CourseEntity course = findCourse(courseId);

        CourseGetRes res =
                CourseGetRes.builder()
                        .title(course.getTitle())
                        .content(course.getContent())
                        .build();

        return res;
    }

    @Transactional
    public CourseUpdateRes updateCourse(Long courseId, CourseUpdateReq req) {
        CourseEntity course = findCourse(courseId);

        course.updateCourse(req.title(), req.content());

        CourseUpdateRes res =
                CourseUpdateRes.builder()
                        .title(course.getTitle())
                        .content(course.getContent())
                        .build();

        return res;
    }

    @Transactional
    public CourseDeleteRes deleteCourse(Long courseId) {
        CourseEntity course = findCourse(courseId);

        courseRepository.delete(course);

        CourseDeleteRes res = CourseDeleteRes.builder().msg(courseId + "번 코스 삭제").build();

        return res;
    }

    public CourseEntity findCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new GlobalException(CourseErrorCode.COURSE_NOT_FOUND));
    }
}
