package com.b6.mypaldotrip.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.exception.EmailErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.EmailAuth;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailUtilTest implements CommonTest {
    @InjectMocks EmailUtil emailUtil;
    @Mock JavaMailSender javaMailSender;
    @Mock EmailAuthService emailAuthService;

    @Test
    @DisplayName("메시지 전송 테스트")
    void 메시지전송1() {
        // given
        MimeMessage mockMessage = mock(MimeMessage.class);
        EmailAuth emailAuth = EmailAuth.builder().email(TEST_EMAIL).code("testCode").build();
        given(javaMailSender.createMimeMessage()).willReturn(mockMessage);
        given(emailAuthService.save(any(EmailAuth.class))).willReturn(emailAuth);
        // when
        String email = emailUtil.sendMessage(TEST_EMAIL, "테스트 인증 코드입니다.");
        // then
        assertThat(email).isEqualTo(TEST_EMAIL);
    }

    @Nested
    @DisplayName("코드 검증 테스트")
    class 코드검증 {
        @Test
        @DisplayName("코드 검증 테스트 성공")
        void 코드검증1() {
            // given
            EmailAuth emailAuth = EmailAuth.builder().email(TEST_EMAIL).code("testCode").build();
            given(emailAuthService.findById(any())).willReturn(emailAuth);
            // when
            emailUtil.verifyCode(TEST_EMAIL, "testCode");
            // then
            verify(emailAuthService).successVerify(emailAuth);
        }

        @Test
        @DisplayName("코드 검증 테스트 실패")
        void 코드검증2() {
            // given
            EmailAuth emailAuth = EmailAuth.builder().email(TEST_EMAIL).code("testCode").build();
            given(emailAuthService.findById(any())).willReturn(emailAuth);
            // when
            var exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                emailUtil.verifyCode(TEST_EMAIL, "wrong-code");
                            });
            // then
            assertThat(exception.getResultCode()).isEqualTo(EmailErrorCode.CODE_MISMATCH);
        }
    }
}
