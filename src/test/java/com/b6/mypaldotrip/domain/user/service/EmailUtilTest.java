package com.b6.mypaldotrip.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.store.entity.EmailAuth;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailUtilTest implements CommonTest {
    @InjectMocks EmailUtil emailUtil;
    @Mock
    JavaMailSender javaMailSender;
    @Mock
    EmailAuthService emailAuthService;

    @Test
    @DisplayName("메시지 전송 테스트")
    void 메시지전송1 (){
        //given
        MimeMessage mockMessage = mock(MimeMessage.class);
        EmailAuth emailAuth = EmailAuth.builder().email(TEST_EMAIL).code("testCode").build();
        given(javaMailSender.createMimeMessage()).willReturn(mockMessage);
        given(emailAuthService.save(any(EmailAuth.class))).willReturn(emailAuth);
        //when
        String email =emailUtil.sendMessage(TEST_EMAIL,"테스트 인증 코드입니다.");
        //then
        assertThat(email).isEqualTo(TEST_EMAIL);
    }
}