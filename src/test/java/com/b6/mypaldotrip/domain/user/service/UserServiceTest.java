package com.b6.mypaldotrip.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserListReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserUpdateReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserDeleteRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserGetProfileRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserUpdateRes;
import com.b6.mypaldotrip.domain.user.exception.UserErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements CommonTest {
    @InjectMocks private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private S3Provider s3Provider;
    @Mock private EmailAuthService emailAuthService;

    @Nested
    @DisplayName("회원가입 테스트")
    class 회원가입 {
        @Test
        @DisplayName("회원가입 테스트 성공")
        void 회원가입1() {
            // given
            UserSignUpReq req =
                    UserSignUpReq.builder()
                            .email(TEST_EMAIL)
                            .username(TEST_USERNAME)
                            .password(TEST_PASSWORD)
                            .build();
            given(emailAuthService.isEmailVerified(any())).willReturn(true);

            // when
            when(userRepository.save(any())).thenReturn(TEST_USER);
            UserSignUpRes res = userService.signup(req);

            // then
            assertThat(res.email()).isEqualTo(TEST_EMAIL);
            verify(passwordEncoder).encode(any());
        }

        @Test
        @DisplayName("회원가입 테스트 실패 - 이메일 검증 미완료")
        void 회원가입2() {
            // given
            UserSignUpReq req =
                    UserSignUpReq.builder()
                            .email(TEST_EMAIL)
                            .username(TEST_USERNAME)
                            .password(TEST_PASSWORD)
                            .build();
            given(emailAuthService.isEmailVerified(any())).willReturn(false);

            // when
            var exception = assertThrows(GlobalException.class, () -> userService.signup(req));

            // then
            assertThat(exception.getResultCode()).isEqualTo(UserErrorCode.BEFORE_EMAIL_VALIDATION);
        }
    }

    @Nested
    @DisplayName("회원탈퇴 테스트")
    class 회원탈퇴{
        @Test
        @DisplayName("회원탈퇴 테스트 성공 - 파일 있음")
        void 회원탈퇴1 (){
            //given
            String message = "유저, 유저파일 삭제";
            given(userRepository.findById(TEST_USERID)).willReturn(Optional.ofNullable(TEST_USER));

            //when
            UserDeleteRes res = userService.deleteUser(TEST_USERID);

            //then
            assertThat(res.message()).isEqualTo(message);
        }
        @Test
        @DisplayName("회원탈퇴 테스트 성공 - 파일 없음")
        void 회원탈퇴2 (){
            //given
            String message = "유저 삭제, 삭제할 파일 없음";
            given(userRepository.findById(TEST_USERID)).willReturn(Optional.ofNullable(TEST_USER));

            //when
            doThrow(new GlobalException(GlobalResultCode.NOT_FOUND_FILE)).when(s3Provider).deleteFile(TEST_USER);
            UserDeleteRes res = userService.deleteUser(TEST_USERID);

            //then
            assertThat(res.message()).isEqualTo(message);
        }
    }

    @Test
    @DisplayName("회원단건조회 테스트 성공")
    void 회원단건조회 (){
        //given
        given(userRepository.findById(TEST_USERID)).willReturn(Optional.of(TEST_USER));

        //when
        UserGetProfileRes res = userService.viewProfile(TEST_USERID);

        //then
        assertThat(res.username()).isEqualTo(TEST_USER.getUsername());
        verify(userRepository).findByIdFetchFollower(any());
        verify(userRepository).findByIdFetchFollowing(any());
        verify(userRepository).findByIdFetchReview(any());
    }

    @Nested
    @DisplayName("회원수정 테스트")
    class 회원수정{
        @Test
        @DisplayName("회원수정 테스트 성공 - 파일 있을 때")
        void 회원수정1 () throws IOException {
            //given
            MockMultipartFile file = new MockMultipartFile(
                "multipartFile", "test.jpg", "image/jpeg", "image bytes".getBytes());
            UserUpdateReq req = UserUpdateReq.builder()
                .username(TEST_USERNAME)
                .introduction(TEST_INTRODUCTION)
                .age(TEST_AGE)
                .password(TEST_PASSWORD)
                .build();
            ObjectMapper objectMapper = new ObjectMapper();
            given(userRepository.findById(TEST_USERID)).willReturn(Optional.of(TEST_USER));

            //when
            when(s3Provider.updateFile(any(),any())).thenReturn(TEST_FILE_URL);
            UserUpdateRes res = userService.updateProfile(file,objectMapper.writeValueAsString(req),TEST_USERID);

            //then
            assertThat(res.fileURL()).isEqualTo(TEST_FILE_URL);
            verify(s3Provider).updateFile(any(), any());
        }
        @Test
        @DisplayName("회원수정 테스트 성공 - 파일 없을 때")
        void 회원수정2 () throws IOException {
            //given
            MockMultipartFile file = new MockMultipartFile(
                "multipartFile", "test.jpg", "image/jpeg", "image bytes".getBytes());
            UserUpdateReq req = UserUpdateReq.builder()
                .username(TEST_USERNAME)
                .introduction(TEST_INTRODUCTION)
                .age(TEST_AGE)
                .password(TEST_PASSWORD)
                .build();
            ObjectMapper objectMapper = new ObjectMapper();
            given(userRepository.findById(TEST_USERID)).willReturn(Optional.of(TEST_USER));

            //when
            when(s3Provider.updateFile(any(),any())).thenThrow(new GlobalException(GlobalResultCode.NOT_FOUND_FILE));
            when(s3Provider.saveFile(any(),any())).thenReturn(TEST_FILE_URL);
            UserUpdateRes res =userService.updateProfile(file,objectMapper.writeValueAsString(req),TEST_USERID);

            //then
            assertThat(res.fileURL()).isEqualTo(TEST_FILE_URL);
            verify(s3Provider).saveFile(any(),any());
        }
    }

    @Test
    @DisplayName("회원목록조회 테스트 성공")
    void 회원목록조회1 (){
        //given
        UserListReq req = UserListReq.builder().build();
        ReflectionTestUtils.setField(TEST_USER, "modifiedAt", LocalDateTime.now());
        List<UserEntity> userList = List.of(TEST_USER);
        given(userRepository.findByDynamicConditions(any(),any())).willReturn(userList);

        //when
        List<UserListRes> res = userService.getUserList(req,new UserDetailsImpl(TEST_USER));

        //then
        assertThat(res.get(0).userId()).isEqualTo(TEST_USER.getUserId());
    }

    @Test
    @DisplayName("내 정보 조회 테스트 성공")
    void 내정보조회 (){
        //given
        given(userRepository.findUsernameByUserId(TEST_USERID)).willReturn(TEST_USERNAME);
        //when
        UserGetProfileRes res = userService.viewMyProfile(TEST_USERID);
        //then
        assertThat(res.username()).isEqualTo(TEST_USER.getUsername());
    }
}
