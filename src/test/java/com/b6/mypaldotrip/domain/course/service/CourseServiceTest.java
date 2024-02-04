package com.b6.mypaldotrip.domain.course.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.course.CourseTest;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseUpdateReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseDeleteRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseGetRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseUpdateRes;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseListWrapper;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import com.b6.mypaldotrip.domain.courseFile.store.repository.CourseFileRepository;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.tripCourse.store.repository.TripCourseRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest implements CourseTest {

    @Mock private TripService tripService;
    @Mock private CityService cityService;
    @Mock private S3Provider s3Provider;
    @Mock private CourseRepository courseRepository;
    @Mock private CourseFileRepository courseFileRepository;
    @Mock private TripCourseRepository tripCourseRepository;
    @InjectMocks private CourseService courseService;

    @Nested
    @DisplayName("코스 생성 테스트")
    class 코스생성 {

        @Test
        @DisplayName("코스 생성 성공")
        void 코스생성() throws IOException {
            // given
            CourseSaveReq req =
                    CourseSaveReq.builder()
                            .title(TEST_COURSE_TITLE)
                            .content(TEST_COURSE_CONTENT)
                            .cityName("testCityName")
                            .tripNames(List.of("testTripName"))
                            .build();
            String reqStr = new ObjectMapper().writeValueAsString(req);

            given(s3Provider.saveFile(any(MultipartFile.class), anyString()))
                    .willReturn(TEST_COURSE_THUMBNAIL_URL);
            given(cityService.findByCityName(anyString())).willReturn(CityEntity.builder().build());
            given(courseRepository.save(any(CourseEntity.class))).willReturn(TEST_COURSE);
            given(tripService.findTripByName(anyString())).willReturn(TripEntity.builder().build());
            // when
            CourseSaveRes res = courseService.saveCourse(reqStr, TEST_USER, TEST_FILE);
            // then
            assertEquals(TEST_COURSE_TITLE, res.title());
            assertEquals(TEST_COURSE_CONTENT, res.content());
        }
    }

    @Nested
    @DisplayName("코스 조회")
    class 코스조회 {

        @Test
        @DisplayName("코스 목록 조회 - 로그인시")
        void 코스목록조회() {
            // given
            CourseSearchReq req = CourseSearchReq.builder().build();

            UserDetailsImpl userDetails = new UserDetailsImpl(TEST_USER);

            List<CourseEntity> courseEntities = List.of(TEST_COURSE);

            Pageable pageable = PageRequest.of(0, 10);
            Page<CourseEntity> coursePage =
                    new PageImpl<>(courseEntities, pageable, courseEntities.size());
            when(courseRepository.getCourseListByDynamicConditions(
                            any(), any(), any(), any(), any()))
                    .thenReturn(coursePage);

            // when
            CourseListWrapper courseListWrapper =
                    courseService.getCourseListByDynamicConditions(0, 10, req, userDetails);

            // then
            assertEquals(courseEntities.size(), courseListWrapper.courseListResList().size());
            assertEquals(
                    TEST_COURSE.getTitle(), courseListWrapper.courseListResList().get(0).title());
        }

        @Test
        @DisplayName("코스 목록 조회 - 비로그인시")
        void 코스목록조회2() {
            // given
            CourseSearchReq req = CourseSearchReq.builder().build();

            UserDetailsImpl userDetails = null;

            List<CourseEntity> courseEntities = List.of(TEST_COURSE);

            Pageable pageable = PageRequest.of(0, 10);
            Page<CourseEntity> coursePage =
                    new PageImpl<>(courseEntities, pageable, courseEntities.size());
            when(courseRepository.getCourseListByDynamicConditions(
                            any(), any(), any(), any(), any()))
                    .thenReturn(coursePage);

            // when
            CourseListWrapper courseListWrapper =
                    courseService.getCourseListByDynamicConditions(0, 10, req, userDetails);

            // then
            assertEquals(courseEntities.size(), courseListWrapper.courseListResList().size());
            assertEquals(
                    TEST_COURSE.getTitle(), courseListWrapper.courseListResList().get(0).title());
        }

        @Test
        @DisplayName("코스 단건 조회")
        void 코스단건조회() {
            // given
            Long courseId = 1L;

            when(courseRepository.findById(courseId)).thenReturn(Optional.ofNullable(TEST_COURSE));

            // when
            CourseGetRes res = courseService.getCourse(courseId);

            // then
            assertEquals(courseId, res.courseId());
        }
    }

    @Test
    @DisplayName("코스 수정")
    void 코스수정() {
        // given
        Long courseId = 1L;
        CourseUpdateReq req =
                CourseUpdateReq.builder()
                        .title(TEST_COURSE_TITLE)
                        .content(TEST_COURSE_CONTENT)
                        .cityName("updated cityName")
                        .tripNames(List.of("updated tripNames"))
                        .build();
        CityEntity city = CityEntity.builder().build();
        TripEntity trip = TripEntity.builder().build();
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(TEST_COURSE));
        when(cityService.findByCityName(any())).thenReturn(city);
        when(tripService.findTripByName(any())).thenReturn(trip);

        // when
        CourseUpdateRes res = courseService.updateCourse(courseId, req, TEST_USER);

        // then
        assertEquals(TEST_COURSE.getTitle(), res.title());
        assertEquals(TEST_COURSE.getContent(), res.content());
    }

    @Nested
    @DisplayName("코스 삭제")
    class 파일삭제 {

        @Test
        @DisplayName("코스 삭제 - 파일 X")
        void 코스삭제() {
            // given
            Long courseId = 1L;
            String msg = courseId + "번 코스 삭제, 삭제할 파일 없음";
            given(courseRepository.findById(courseId)).willReturn(Optional.of(TEST_COURSE));

            // when
            CourseDeleteRes res = courseService.deleteCourse(courseId, TEST_USER);
            // then
            verify(courseRepository).delete(TEST_COURSE);
            assertEquals(msg, res.msg());
        }

        @Test
        @DisplayName("코스 삭제 - 파일 O")
        void 코스삭제2() {
            // given
            Long courseId = 1L;
            String msg = courseId + "번 코스, 파일 삭제";
            given(courseRepository.findById(courseId)).willReturn(Optional.of(ANOTHER_COURSE));
            ANOTHER_COURSE.getFiles().add(COURSE_FILE);

            // when
            CourseDeleteRes res = courseService.deleteCourse(courseId, TEST_USER);
            // then
            verify(courseRepository).delete(ANOTHER_COURSE);
            assertEquals(msg, res.msg());
        }
    }
}
