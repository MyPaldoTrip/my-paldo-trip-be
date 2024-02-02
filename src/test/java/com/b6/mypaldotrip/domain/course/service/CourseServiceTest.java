package com.b6.mypaldotrip.domain.course.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.course.CourseTest;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSaveReq;
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.repository.CourseRepository;
import com.b6.mypaldotrip.domain.courseFile.store.repository.CourseFileRepository;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.tripCourse.store.repository.TripCourseRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
}
