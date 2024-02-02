package com.b6.mypaldotrip.domain.course.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.repository.CommentRepository;
import com.b6.mypaldotrip.domain.course.CourseTest;
import com.b6.mypaldotrip.domain.course.CustomCourseRepositoryTestConfig;
import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import com.b6.mypaldotrip.domain.follow.store.entity.FollowerEntity;
import com.b6.mypaldotrip.domain.like.store.entity.LikeEntity;
import com.b6.mypaldotrip.domain.like.store.repository.LikeRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import java.time.LocalDateTime;
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

    @Autowired private CourseRepository courseRepository;
    @Autowired private CustomCourseRepository customCourseRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private LikeRepository likeRepository;
    @Autowired private CommentRepository commentRepository;
    private UserEntity user1;
    private UserEntity user2;
    private UserEntity user3;
    private CourseEntity course1;
    private CourseEntity course2;
    private CourseEntity course3;
    private CityEntity city;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(setUser(1L));
        user2 = userRepository.save(setUser(2L));
        user3 = userRepository.save(setUser(3L));

        city =
                cityRepository.save(
                        CityEntity.builder()
                                .provinceName("testProvinceName")
                                .cityName("cityName")
                                .build());

        course1 = courseRepository.save(setCourse(user1, city));
        course2 = courseRepository.save(setCourse(user2, null));
        course3 = courseRepository.save(setCourse(user3, null));

        setLike(user1, course1);
        setLike(user2, course1);
        setLike(user1, course2);

        setComment(user1, course1);
        setComment(user1, course1);
        setComment(user1, course2);

        pageable = PageRequest.of(0, 5);
    }

    @Nested
    @DisplayName("코스목록 동적조회 테스트")
    class 코스목록동적조회 {

        @Test
        @DisplayName("도시별로 찾기")
        void 동적조회1() {
            // given
            CourseSearchReq req =
                    CourseSearchReq.builder()
                            .courseSort(CourseSort.MODIFIED)
                            .filterByCityName("cityName")
                            .filterByFollowing(false)
                            .build();
            // when
            Page<CourseEntity> res =
                    customCourseRepository.getCourseListByDynamicConditions(
                            pageable,
                            req.courseSort(),
                            req,
                            user1.getUserId(),
                            req.filterByFollowing());
            // then
            assertThat(res.getContent().get(0).getCityEntity().getCityName())
                    .isEqualTo(city.getCityName());
            assertThat(res.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("팔로우한 유저만 보기")
        void 동적조회2() {
            // given
            CourseSearchReq req =
                    CourseSearchReq.builder()
                            .courseSort(CourseSort.MODIFIED)
                            .filterByCityName("")
                            .filterByFollowing(true)
                            .build();
            ReflectionTestUtils.setField(
                    user1,
                    "followerList",
                    List.of(FollowerEntity.builder().user(user1).followedUser(user2).build()));
            // when
            Page<CourseEntity> res =
                    customCourseRepository.getCourseListByDynamicConditions(
                            pageable,
                            req.courseSort(),
                            req,
                            user2.getUserId(),
                            req.filterByFollowing());
            // then
            assertThat(
                            res.getContent()
                                    .get(0)
                                    .getUserEntity()
                                    .getFollowerList()
                                    .get(0)
                                    .getFollowedUser())
                    .isEqualTo(user2);
            assertThat(res.getTotalElements()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("코스목록 동적정렬 테스트")
    class 코스목록동적정렬 {
        @Test
        @DisplayName("최근 수정순 정렬")
        void 동적정렬1() {
            // given
            CourseSearchReq req =
                    CourseSearchReq.builder()
                            .courseSort(CourseSort.MODIFIED)
                            .filterByCityName("")
                            .filterByFollowing(false)
                            .build();
            ReflectionTestUtils.setField(course1, "modifiedAt", LocalDateTime.now());
            ReflectionTestUtils.setField(course2, "modifiedAt", LocalDateTime.now().plusSeconds(1));
            ReflectionTestUtils.setField(course3, "modifiedAt", LocalDateTime.now().plusSeconds(2));
            // when
            Page<CourseEntity> res =
                    customCourseRepository.getCourseListByDynamicConditions(
                            pageable, req.courseSort(), req, -1L, req.filterByFollowing());
            // then
            assertThat(res.getContent().get(0).getModifiedAt())
                    .isAfterOrEqualTo(res.getContent().get(1).getModifiedAt());
            assertThat(res.getContent().get(1).getModifiedAt())
                    .isAfterOrEqualTo(res.getContent().get(2).getModifiedAt());
        }

        @Test
        @DisplayName("유저레벨높은순 정렬")
        void 동적정렬2() {
            // given
            CourseSearchReq req =
                    CourseSearchReq.builder()
                            .courseSort(CourseSort.LEVEL)
                            .filterByCityName("")
                            .filterByFollowing(false)
                            .build();
            ReflectionTestUtils.setField(user1, "level", 1L);
            ReflectionTestUtils.setField(user2, "level", 2L);
            ReflectionTestUtils.setField(user3, "level", 3L);
            // when
            Page<CourseEntity> res =
                    customCourseRepository.getCourseListByDynamicConditions(
                            pageable, req.courseSort(), req, -1L, req.filterByFollowing());
            // then
            assertThat(res.getContent().get(0).getUserEntity().getLevel())
                    .isGreaterThan(res.getContent().get(1).getUserEntity().getLevel());
            assertThat(res.getContent().get(1).getUserEntity().getLevel())
                    .isGreaterThan(res.getContent().get(2).getUserEntity().getLevel());
        }

        @Test
        @DisplayName("좋아요많은순 정렬")
        void 동적정렬3() {
            // given
            CourseSearchReq req =
                    CourseSearchReq.builder()
                            .courseSort(CourseSort.LIKE)
                            .filterByCityName("")
                            .filterByFollowing(false)
                            .build();
            // when
            Page<CourseEntity> res =
                    customCourseRepository.getCourseListByDynamicConditions(
                            pageable, req.courseSort(), req, -1L, req.filterByFollowing());
            // then
            assertThat(res.getContent().get(0).getLikes().size())
                    .isGreaterThan(res.getContent().get(1).getLikes().size());
            assertThat(res.getContent().get(1).getLikes().size())
                    .isGreaterThan(res.getContent().get(2).getLikes().size());
        }

        @Test
        @DisplayName("댓글 많은순 정렬")
        void 동적정렬4() {
            // given
            CourseSearchReq req =
                    CourseSearchReq.builder()
                            .courseSort(CourseSort.COMMENT)
                            .filterByCityName("")
                            .filterByFollowing(false)
                            .build();
            // when
            Page<CourseEntity> res =
                    customCourseRepository.getCourseListByDynamicConditions(
                            pageable, req.courseSort(), req, -1L, req.filterByFollowing());
            // then
            assertThat(res.getContent().get(0).getComments().size())
                    .isGreaterThan(res.getContent().get(1).getComments().size());
            assertThat(res.getContent().get(1).getComments().size())
                    .isGreaterThan(res.getContent().get(2).getComments().size());
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

    private CourseEntity setCourse(UserEntity user, CityEntity city) {
        return CourseEntity.builder().userEntity(user).cityEntity(city).build();
    }

    protected void setLike(UserEntity user, CourseEntity course) {
        LikeEntity like =
                likeRepository.save(LikeEntity.builder().user(user).course(course).build());
        course.getLikes().add(like);
    }

    protected void setComment(UserEntity user, CourseEntity course) {
        CommentEntity comment =
                commentRepository.save(
                        CommentEntity.builder().userEntity(user).courseEntity(course).build());
        course.getComments().add(comment);
    }
}
