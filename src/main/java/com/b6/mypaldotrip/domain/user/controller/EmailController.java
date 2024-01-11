package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.controller.dto.request.EmailSendReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.EmailSendRes;
import com.b6.mypaldotrip.domain.user.service.EmailService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/users/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<EmailSendRes>> sendEmail(
            @Valid @RequestBody EmailSendReq req) {
        EmailSendRes res = emailService.sendEmail(req);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
