package com.b6.mypaldotrip.domain.course.service;

import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
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
import com.b6.mypaldotrip.domain.courseFile.store.entity.CourseFileEntity;
import com.b6.mypaldotrip.domain.courseFile.store.repository.CourseFileRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CityService cityService;
    private final CourseFileRepository courseFileRepository;
    private final S3Provider s3Provider;

    @Transactional
    public CourseSaveRes saveCourse(CourseSaveReq req, UserEntity user, MultipartFile multipartFile)
            throws IOException {
        CityEntity city = cityService.findByCityName(req.cityName());

        CourseEntity course =
                CourseEntity.builder()
                        .title(req.title())
                        .content(req.content())
                        .userEntity(user)
                        .cityEntity(city)
                        .build();

        CourseFileEntity courseFileEntity = CourseFileEntity.builder().build();

        String fileUrl;
        try {
            fileUrl = s3Provider.updateFile(courseFileEntity, multipartFile);
        } catch (GlobalException e) {
            fileUrl = s3Provider.saveFile(multipartFile, "course");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        courseFileEntity = CourseFileEntity.builder().courseEntity(course).fileURL(fileUrl).build();

        courseFileRepository.save(courseFileEntity);

        courseRepository.save(course);

        CourseSaveRes res =
                CourseSaveRes.builder()
                        .title(course.getTitle())
                        .content(course.getContent())
                        .build();

        return res;
    }

    public List<CourseListRes> getCourseListByDynamicConditions(
            int page, int size, CourseSearchReq req, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        CourseSort courseSort = req.courseSort() != null ? req.courseSort() : CourseSort.MODIFIED;
        Boolean filterByFollowing = req.filterByFollowing();

        List<CourseListRes> res =
                courseRepository
                        .getCourseListByDynamicConditions(
                                pageable, courseSort, req, userId, filterByFollowing)
                        .stream()
                        .map(
                                c ->
                                        CourseListRes.builder()
                                                .courseId(c.getCourseId())
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

        for (CourseFileEntity courseFileEntity : course.getFiles()) {
            try {
                s3Provider.deleteFile(courseFileEntity);
            } catch (GlobalException e) {
                return CourseDeleteRes.builder().msg(courseId + "번 코스 삭제, 삭제할 파일 없음").build();
            }
        }

        CourseDeleteRes res = CourseDeleteRes.builder().msg(courseId + "번 코스, 파일 삭제").build();

        return res;
    }

    public CourseEntity findCourse(Long courseId) {
        return courseRepository
                .findById(courseId)
                .orElseThrow(() -> new GlobalException(CourseErrorCode.COURSE_NOT_FOUND));
    }

    private static void validateAuth(UserEntity userEntity, CourseEntity course) {
        if (userEntity.getUserRole() == UserRole.ROLE_USER
                && !Objects.equals(course.getUserEntity().getUserId(), userEntity.getUserId())) {
            throw new GlobalException(CourseErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
