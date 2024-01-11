package com.b6.mypaldotrip.domain.user.service;

import com.b6.mypaldotrip.domain.user.exception.EmailErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.EmailAuth;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "이메일 유틸")
@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final EmailAuthService emailAuthService;

    @Value("${spring.mail.username}")
    private String sendEmail;

    public String sendMessage(String recipientEmail, String subject) {
        try {
            String code = UUID.randomUUID().toString().substring(0, 8);
            MimeMessage message = createMessage(recipientEmail, subject, code);

            EmailAuth emailAuth =
                    emailAuthService.save(
                            EmailAuth.builder().email(recipientEmail).code(code).build());
            javaMailSender.send(message);
            return emailAuth.getEmail();
        } catch (MessagingException e) {
            log.warn(e.getMessage());
            throw new GlobalException(EmailErrorCode.SEND_MESSAGE_ERROR);
        }
    }

    private MimeMessage createMessage(String recipientEmail, String subject, String code)
            throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(sendEmail);
        message.addRecipients(RecipientType.TO, recipientEmail);
        message.setSubject(subject);
        message.setText(code);

        return message;
    }

    @Transactional
    public void verifyCode(String recipientEmail, String code) {
        EmailAuth emailAuth = emailAuthService.findById(recipientEmail);
        if (Objects.equals(emailAuth.getCode(), code)) {
            emailAuth.verifyComplete();
        } else {
            throw new GlobalException(EmailErrorCode.COND_MISMATCH);
        }
    }
}
