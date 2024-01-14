package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationSubmitReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationSubmitRes;
import com.b6.mypaldotrip.domain.user.service.ApplicationService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/users/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<ApplicationSubmitRes>> submit(
            @Valid @RequestBody ApplicationSubmitReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ApplicationSubmitRes res = applicationService.submit(req, userDetails.getUserEntity());
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }
}
