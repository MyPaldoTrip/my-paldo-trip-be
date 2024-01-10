package com.b6.mypaldotrip.domain.like.controller.dto;

import com.b6.mypaldotrip.domain.like.controller.dto.response.LikeToggleRes;
import com.b6.mypaldotrip.domain.like.service.LikeService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/courses/{courseId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<LikeToggleRes>> toggleLike(
            @PathVariable Long courseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LikeToggleRes res = likeService.toggleLike(courseId, userDetails.getUser());

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
