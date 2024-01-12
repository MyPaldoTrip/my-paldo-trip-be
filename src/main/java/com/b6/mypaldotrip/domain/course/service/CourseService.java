package com.b6.mypaldotrip.domain.course.service;

import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseUpdateReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseDeleteRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseGetRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseListRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseUpdateRes;
import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CityRepository cityRepository;

    public CourseSaveRes saveCourse(CourseSaveReq req, UserEntity user) {
        CityEntity city =
                cityRepository
                        .findByCityName(req.cityName())
                        .orElseThrow(() -> new GlobalException(CityErrorCode.CITY_NOT_FOUND));

        CourseEntity course =
                CourseEntity.builder()
                        .title(req.title())
                        .content(req.content())
                        .userEntity(user)
                        .cityEntity(city)
                        .build();

        courseRepository.save(course);

        CourseSaveRes res =
                CourseSaveRes.builder()
                        .title(course.getTitle())
                        .content(course.getContent())
                        .build();

        return res;
    }

    public List<CourseListRes> getCourseListByDynamicConditions(
            int page, int size, CourseSearchReq req, UserEntity userEntity) {
        Pageable pageable = PageRequest.of(page, size);
        CourseSort courseSort = req.courseSort() != null ? req.courseSort() : CourseSort.MODIFIED;

        List<CourseListRes> res =
                courseRepository
                        .getCourseListByDynamicConditions(pageable, courseSort, req, userEntity)
                        .stream()
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
    public CourseUpdateRes updateCourse(Long courseId, CourseUpdateReq req, UserEntity userEntity) {
        CourseEntity course = findCourse(courseId);

        validateAuth(userEntity, course);

        course.updateCourse(req.title(), req.content());

        CourseUpdateRes res =
                CourseUpdateRes.builder()
                        .title(course.getTitle())
                        .content(course.getContent())
                        .build();

        return res;
    }

    @Transactional
    public CourseDeleteRes deleteCourse(Long courseId, UserEntity userEntity) {
        CourseEntity course = findCourse(courseId);

        validateAuth(userEntity, course);

        courseRepository.delete(course);

        CourseDeleteRes res = CourseDeleteRes.builder().msg(courseId + "번 코스 삭제").build();

        return res;
    }

    public CourseEntity findCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new GlobalException(CourseErrorCode.COURSE_NOT_FOUND));
    }

    private static void validateAuth(UserEntity userEntity, CourseEntity course) {
        if (userEntity.getUserRole() == UserRole.ROLE_USER
                && !Objects.equals(course.getUserEntity(), userEntity)) {
            throw new GlobalException(CourseErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
