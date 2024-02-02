package com.b6.mypaldotrip.domain.course.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.domain.course.CourseTest;
import com.b6.mypaldotrip.domain.course.CustomCourseRepositoryTestConfig;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import com.b6.mypaldotrip.domain.follow.store.entity.FollowingEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@Import(CustomCourseRepositoryTestConfig.class)
@ActiveProfiles("test")
class CustomCourseRepositoryTest implements CourseTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CustomCourseRepository customCourseRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private UserRepository userRepository;
    private CourseEntity course1;
    private CourseEntity course2;
    private Pageable pageable;

    private CityEntity city1;
    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(setUser(1L));
        user2 = userRepository.save(setUser(2L));
        city1 = CityEntity.builder().provinceName("testProvinceName").cityName("cityName").build();
        cityRepository.save(city1);
        course1 = CourseEntity.builder().userEntity(user1).cityEntity(city1).build();
        course2 = CourseEntity.builder().userEntity(user2).build();
        courseRepository.save(course1);
        courseRepository.save(course2);
        pageable = PageRequest.of(0, 5);


    }

    @Nested
    @DisplayName("코스목록 동적조회 테스트")
    class 코스목록동적조회 {

        @Test
        @DisplayName("도시별로 찾기")
        void 동적조회1() {
            // given
            CourseSearchReq req = CourseSearchReq.builder()
                .courseSort(CourseSort.MODIFIED)
                .filterByCityName("cityName")
                .filterByFollowing(false)
                .build();
            // when
            Page<CourseEntity> res = customCourseRepository.getCourseListByDynamicConditions(
                pageable,
                req.courseSort(), req, user1.getUserId(), req.filterByFollowing());
            // then
            assertThat(res.getContent().get(0).getCityEntity().getCityName()).isEqualTo(
                city1.getCityName());
            assertThat(res.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("팔로우한 유저만 보기")
        void 동적조회2() {
            // given
            CourseSearchReq req = CourseSearchReq.builder()
                .courseSort(CourseSort.MODIFIED)
                .filterByFollowing(true)
                .build();
            ReflectionTestUtils.setField(user2, "followingList", List.of(
                FollowingEntity.builder().user(user2).followingUser(user1).build()));
            // when
            Page<CourseEntity> res = customCourseRepository.getCourseListByDynamicConditions(
                pageable, req.courseSort(), req, user2.getUserId(), req.filterByFollowing());
            // then
            assertThat(user2.getFollowingList().get(0).getFollowingUser()).isEqualTo(
                res.getContent().get(0).getUserEntity());
            assertThat(res.getTotalElements()).isEqualTo(1);
        }

    }

    private UserEntity setUser(Long n) {
        return UserEntity.builder()
            .email("user" + n + "@gmail.com")
            .username("user" + n)
            .password(String.valueOf(n))
            .age(n)
            .build();
    }
}