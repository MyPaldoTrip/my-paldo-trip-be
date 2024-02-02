package com.b6.mypaldotrip.domain.user.store.repository;

import static com.b6.mypaldotrip.domain.user.store.entity.UserRole.ROLE_ADMIN;
import static com.b6.mypaldotrip.domain.user.store.entity.UserRole.ROLE_OPERATOR;
import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.follow.store.entity.FollowerEntity;
import com.b6.mypaldotrip.domain.follow.store.entity.FollowingEntity;
import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.UserCustomRepositoryTestConfig;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserListReq;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest
@Import(UserCustomRepositoryTestConfig.class)
@ActiveProfiles("test")
class UserCustomRepositoryTest implements CommonTest {

    @Autowired private UserCustomRepository userCustomRepository;
    @Autowired private UserRepository userRepository;

    private UserEntity saveUser1;
    private UserEntity saveUser2;
    private UserEntity saveUser3;
    private UserEntity saveUser4;
    private UserEntity saveUser5;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(TEST_ANOTHER_USER);
        saveUser1 = userRepository.save(setUser(1L));
        saveUser2 = userRepository.save(setUser(2L));
        saveUser3 = userRepository.save(setUser(3L));
        saveUser4 = userRepository.save(setUser(4L));
        saveUser5 = userRepository.save(setUser(5L));
        ReflectionTestUtils.setField(saveUser2, "userRole", ROLE_OPERATOR);
        ReflectionTestUtils.setField(saveUser3, "userRole", ROLE_ADMIN);
    }

    @Nested
    @DisplayName("회원동적조회 테스트")
    class 회원동적조회 {

        @Test
        @DisplayName("회원동적조회 테스트 - 나이")
        void 회원동적조회1() {
            // given
            UserListReq req = UserListReq.builder().page(0).size(5).ageCondition(1L).build();
            // when
            List<UserEntity> userList =
                    userCustomRepository.findByDynamicConditions(
                            req, new UserDetailsImpl(TEST_USER));
            // then
            assertThat(userList.get(0).getAge()).isEqualTo(saveUser1.getAge()).isEqualTo(1);
            assertThat(userList.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("회원동적조회 테스트 - 레벨")
        void 회원동적조회2() {
            // given
            UserListReq req = UserListReq.builder().page(0).size(5).levelCondition(1L).build();
            // when
            List<UserEntity> userList =
                    userCustomRepository.findByDynamicConditions(
                            req, new UserDetailsImpl(TEST_USER));
            // then
            assertThat(userList.get(0).getLevel()).isEqualTo(1);
            assertThat(userList.size()).isEqualTo(5);
        }

        @Test
        @DisplayName("회원동적조회 테스트 - 역할")
        void 회원동적조회3() {
            // given
            UserListReq req =
                    UserListReq.builder().page(0).size(5).userRoleCondition(ROLE_ADMIN).build();
            // when
            List<UserEntity> userList =
                    userCustomRepository.findByDynamicConditions(
                            req, new UserDetailsImpl(TEST_USER));
            // then
            assertThat(userList.get(0).getUserRole())
                    .isEqualTo(saveUser3.getUserRole())
                    .isEqualTo(ROLE_ADMIN);
            assertThat(userList.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("회원동적조회 테스트 - 팔로워")
        void 회원동적조회4() {
            // given
            ReflectionTestUtils.setField(
                    testUser,
                    "followerList",
                    List.of(
                            FollowerEntity.builder()
                                    .user(testUser)
                                    .followedUser(saveUser4)
                                    .build()));
            UserListReq req = UserListReq.builder().page(0).size(5).followerCondition(true).build();
            // when
            List<UserEntity> userList =
                    userCustomRepository.findByDynamicConditions(
                            req, new UserDetailsImpl(testUser));
            // then
            assertThat(userList.get(0)).isEqualTo(saveUser4);
            assertThat(userList.size()).isEqualTo(1);
        }

        @Test
        @DisplayName("회원동적조회 테스트 - 팔로잉")
        void 회원동적조회5() {
            // given
            ReflectionTestUtils.setField(
                    testUser,
                    "followingList",
                    List.of(
                            FollowingEntity.builder()
                                    .user(testUser)
                                    .followingUser(saveUser5)
                                    .build()));
            UserListReq req =
                    UserListReq.builder().page(0).size(5).followingCondition(true).build();
            // when
            List<UserEntity> userList =
                    userCustomRepository.findByDynamicConditions(
                            req, new UserDetailsImpl(testUser));
            // then
            assertThat(userList.get(0)).isEqualTo(saveUser5);
            assertThat(userList.size()).isEqualTo(1);
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
