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
import com.b6.mypaldotrip.domain.course.store.entity.CourseListWrapper;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import com.b6.mypaldotrip.domain.courseFile.store.entity.CourseFileEntity;
import com.b6.mypaldotrip.domain.courseFile.store.repository.CourseFileRepository;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.tripCourse.store.entity.TripCourseEntity;
import com.b6.mypaldotrip.domain.tripCourse.store.repository.TripCourseRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CityService cityService;
    private final TripService tripService;
    private final CourseFileRepository courseFileRepository;
    private final TripCourseRepository tripCourseRepository;
    private final S3Provider s3Provider;

    @Transactional
    public CourseSaveRes saveCourse(String reqStr, UserEntity user, MultipartFile multipartFile)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CourseSaveReq req = objectMapper.readValue(reqStr, CourseSaveReq.class);
        String fileUrl = s3Provider.saveFile(multipartFile, "course");

        CityEntity city = cityService.findByCityName(req.cityName());

        CourseEntity course =
                CourseEntity.builder()
                        .title(req.title())
                        .content(req.content())
                        .userEntity(user)
                        .cityEntity(city)
                        .thumbnailUrl(fileUrl)
                        .build();

        CourseEntity savedCourse = courseRepository.save(course);

        for (String tripName : req.tripNames()) {
            TripEntity trip = tripService.findTripByName(tripName);
            TripCourseEntity tripCourse =
                    TripCourseEntity.builder().trip(trip).course(savedCourse).build();
            tripCourseRepository.save(tripCourse);
        }

        CourseFileEntity courseFileEntity =
                CourseFileEntity.builder().courseEntity(savedCourse).fileURL(fileUrl).build();
        courseFileRepository.save(courseFileEntity);

        CourseSaveRes res =
                CourseSaveRes.builder()
                        .courseId(savedCourse.getCourseId())
                        .title(savedCourse.getTitle())
                        .content(savedCourse.getContent())
                        .build();

        return res;
    }

    @Transactional
    public List<CourseListRes> getCourseListByDynamicConditions(
    public CourseListWrapper getCourseListByDynamicConditions(
            int page, int size, CourseSearchReq req, UserDetailsImpl userDetails) {
        Long userId;
        if (userDetails != null) {
            userId = userDetails.getUserEntity().getUserId();
        } else {
            userId = -1L;
        }
        Pageable pageable = PageRequest.of(page, size);
        CourseSort courseSort = req.courseSort() != null ? req.courseSort() : CourseSort.MODIFIED;
        Boolean filterByFollowing = req.filterByFollowing();

        Page<CourseEntity> val =
                courseRepository.getCourseListByDynamicConditions(
                        pageable, courseSort, req, userId, filterByFollowing);
        courseRepository.fetchComments(userId, req, filterByFollowing);
        courseRepository.fetchLikes(userId, req, filterByFollowing);
        List<CourseListRes> res =
                val.stream()
                        .map(
                                courseEntity ->
                                        CourseListRes.builder()
                                                .courseId(courseEntity.getCourseId())
                                                .username(
                                                        courseEntity.getUserEntity().getUsername())
                                                .title(courseEntity.getTitle())
                                                .content(courseEntity.getContent())
                                                .totalPage(val.getTotalPages())
                                                .level(courseEntity.getUserEntity().getLevel())
                                                .createdAt(courseEntity.getCreatedAt())
                                                .commentCount(courseEntity.getComments().size())
                                                .likeCount(courseEntity.getLikes().size())
                                                .thumbnailUrl(courseEntity.getThumbnailUrl())
                                                .build())
                        .toList();
        CourseListWrapper courseListWrapper = CourseListWrapper.builder().courseListResList(res).build();
        return courseListWrapper;
    }

    @Transactional
    public CourseGetRes getCourse(Long courseId) {
        CourseEntity course = findCourse(courseId);

        List<String> urlList = new ArrayList<>();
        List<Long> tripIds = new ArrayList<>();
        List<String> tripNames = new ArrayList<>();

        for (CourseFileEntity courseFileEntity : course.getFiles()) {
            urlList.add(courseFileEntity.getFileURL());
        }
        for (TripCourseEntity tripCourse : course.getTripCourses()) {
            tripIds.add(tripCourse.getTrip().getTripId());
            tripNames.add(tripCourse.getTrip().getName());
        }

        CourseGetRes res =
                CourseGetRes.builder()
                        .courseId(courseId)
                        .username(course.getUserEntity().getUsername())
                        .title(course.getTitle())
                        .content(course.getContent())
                        .cityName(course.getCityEntity().getCityName())
                        .fileURL(urlList)
                        .relatedTripIds(tripIds)
                        .relatedTripNames(tripNames)
                        .createdAt(course.getCreatedAt())
                        .build();

        return res;
    }

    @Transactional
    public CourseUpdateRes updateCourse(Long courseId, CourseUpdateReq req, UserEntity userEntity) {
        CourseEntity course = findCourse(courseId);
        validateAuth(userEntity, course);
        CityEntity cityEntity = cityService.findByCityName(req.cityName());
        String thumbnailUrl;
        if (!course.getFiles().isEmpty()) {
            thumbnailUrl = course.getFiles().get(0).getFileURL();
        } else {
            thumbnailUrl = null;
        }

        if (req.tripNames() != null) {
            course.clearTripCourses();
            for (String tripName : req.tripNames()) {
                TripEntity trip = tripService.findTripByName(tripName);
                TripCourseEntity tripCourseEntity =
                        TripCourseEntity.builder().course(course).trip(trip).build();
                course.updateTripCourses(tripCourseEntity);
            }
        }

        course.updateCourse(req.title(), req.content(), cityEntity, thumbnailUrl);

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
        if (course.getFiles().isEmpty()) {
            return CourseDeleteRes.builder().msg(courseId + "번 코스 삭제, 삭제할 파일 없음").build();
        } else {
            for (CourseFileEntity courseFileEntity : course.getFiles()) {
                s3Provider.deleteFile(courseFileEntity);
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
