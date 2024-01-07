package com.b6.mypaldotrip.domain.sample.controller;

import com.b6.mypaldotrip.domain.sample.controller.dto.request.SampleReq;
import com.b6.mypaldotrip.domain.sample.controller.dto.response.SampleRes;
import com.b6.mypaldotrip.domain.sample.service.SampleService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${mpt.version}/sample")
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<SampleRes>> saveSample(@RequestBody SampleReq req) {
        SampleRes res = sampleService.saveSample(req);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
            .toResponseEntity();
    }
}
