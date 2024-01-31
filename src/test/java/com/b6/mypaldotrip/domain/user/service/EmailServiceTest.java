package com.b6.mypaldotrip.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.domain.user.controller.dto.request.EmailSendReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.EmailSendRes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest implements CommonTest {

    @InjectMocks EmailService emailService;
    @Mock EmailUtil emailUtil;

    @Test
    @DisplayName("이메일 발송 테스트")
    void 이메일발송 (){
        //given
        EmailSendReq req = EmailSendReq.builder().email(TEST_EMAIL).build();
        //when
        when(emailUtil.sendMessage(any(),any())).thenReturn(TEST_EMAIL);
        EmailSendRes res = emailService.sendEmail(req);
        //then
        assertThat(res.recipientEmail()).isEqualTo(TEST_EMAIL);
    }
}