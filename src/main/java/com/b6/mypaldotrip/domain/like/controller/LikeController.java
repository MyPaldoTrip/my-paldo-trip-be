package com.b6.mypaldotrip.domain.like.controller;

import com.b6.mypaldotrip.domain.like.controller.dto.response.LikeToggleRes;
import com.b6.mypaldotrip.domain.like.service.LikeService;
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

    @PostMapping
    public ResponseEntity<RestResponse<LikeToggleRes>> toggleLike(
            @PathVariable Long courseId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return likeService.toggleLike(courseId, userDetails.getUserEntity());
    }
}
