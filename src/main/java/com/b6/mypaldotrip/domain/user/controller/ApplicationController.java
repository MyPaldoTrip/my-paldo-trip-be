package com.b6.mypaldotrip.domain.user.controller;

import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationCheckReq;
import com.b6.mypaldotrip.domain.user.controller.dto.request.ApplicationSubmitReq;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationConfirmRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetListRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationGetRes;
import com.b6.mypaldotrip.domain.user.controller.dto.response.ApplicationSubmitRes;
import com.b6.mypaldotrip.domain.user.service.ApplicationService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ResponseEntity<RestResponse<List<ApplicationGetListRes>>> getList() {
        List<ApplicationGetListRes> res = applicationService.getList();
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<RestResponse<ApplicationGetRes>> getApplication(
            @PathVariable Long applicationId) {
        ApplicationGetRes res = applicationService.getApplication(applicationId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PatchMapping
    public ResponseEntity<RestResponse<ApplicationConfirmRes>> confirm(
            @Valid @RequestBody ApplicationCheckReq req) {
        ApplicationConfirmRes res = applicationService.confirm(req);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
