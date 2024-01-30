package com.b6.mypaldotrip.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.controller.dto.request.UserSignUpReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.UserSignUpRes;
import com.b6.mypaldotrip.domain.user.exception.UserErrorCode;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements CommonTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private S3Provider s3Provider;
    @Mock
    private EmailAuthService emailAuthService;

    @Nested
    @DisplayName("회원가입 테스트")
    class 회원가입{
        @Test
        @DisplayName("회원가입 테스트 성공")
        void 회원가입1 (){
            //given
            UserSignUpReq req =
                UserSignUpReq.builder()
                    .email(TEST_EMAIL)
                    .username(TEST_USERNAME)
                    .password(TEST_PASSWORD)
                    .build();
            given(emailAuthService.isEmailVerified(any())).willReturn(true);

            //when
            when(userRepository.save(any())).thenReturn(TEST_USER);
            UserSignUpRes res = userService.signup(req);

            //then
            assertThat(res.email()).isEqualTo(TEST_EMAIL);
            verify(passwordEncoder).encode(any());
        }
        @Test
        @DisplayName("회원가입 테스트 실패 - 이메일 검증 미완료")
        void 회원가입2 (){
            //given
            UserSignUpReq req =
                UserSignUpReq.builder()
                    .email(TEST_EMAIL)
                    .username(TEST_USERNAME)
                    .password(TEST_PASSWORD)
                    .build();
            given(emailAuthService.isEmailVerified(any())).willReturn(false);

            //when
            var exception = assertThrows(GlobalException.class,
                () -> userService.signup(req));

            //then
            assertThat(exception.getResultCode()).isEqualTo(UserErrorCode.BEFORE_EMAIL_VALIDATION);
        }
    }
}
