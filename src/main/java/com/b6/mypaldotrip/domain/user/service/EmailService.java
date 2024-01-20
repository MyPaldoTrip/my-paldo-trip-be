package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.controller.dto.request.EmailSendReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.EmailVerifyReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.EmailSendRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.EmailVerifyRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailUtil emailUtil;

    public EmailSendRes sendEmail(EmailSendReq req) {
        String recipientEmail = emailUtil.sendMessage(req.email(), "My Paldo Trip 이메일 인증 코드");
        return EmailSendRes.builder().recipientEmail(recipientEmail).message("인증 코드 발송").build();
    }

    public EmailVerifyRes verifyEmail(EmailVerifyReq req) {
        emailUtil.verifyCode(req.email(), req.code());
        return EmailVerifyRes.builder().message("인증 코드 검증 성공").build();
    }
}
