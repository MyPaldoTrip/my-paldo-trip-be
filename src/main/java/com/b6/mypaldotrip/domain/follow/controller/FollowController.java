package com.b6.mypaldotrip.domain.follow.controller;

import com.b6.mypaldotrip.domain.follow.controller.dto.response.FollowToggleRes;
import com.b6.mypaldotrip.domain.follow.service.FollowService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/users/{userId}/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final VersionConfig versionConfig;

    @PutMapping
    public ResponseEntity<RestResponse<FollowToggleRes>> followToggle(
            @PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FollowToggleRes res =
                followService.followToggle(userId, userDetails.getUserEntity().getUserId());
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
