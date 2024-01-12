package com.b6.mypaldotrip.domain.review.controller;

import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewCreateReq;
import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewListReq;
import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewUpdateReq;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewCreateRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewDeleteRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewUpdateRes;
import com.b6.mypaldotrip.domain.review.service.ReviewService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/${mpt.version}/trips/{tripId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<ReviewCreateRes>> createReview(
            @PathVariable Long tripId,
            @Valid @RequestBody ReviewCreateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewCreateRes res = reviewService.createReview(tripId, req, userDetails);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<ReviewListRes>>> getReviewList(
            @PathVariable Long tripId,
            @RequestBody ReviewListReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails != null ? userDetails.getUserEntity().getUserId() : null;
        List<ReviewListRes> res = reviewService.getReviewList(tripId, req, userId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PatchMapping("/{reviewId}")
    ResponseEntity<RestResponse<ReviewUpdateRes>> updateReview(
            @PathVariable Long tripId,
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewUpdateRes res = reviewService.updateReview(tripId, reviewId, req, userDetails);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{reviewId}")
    ResponseEntity<RestResponse<ReviewDeleteRes>> deleteReview(
            @PathVariable Long tripId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ReviewDeleteRes res = reviewService.deleteReview(tripId, reviewId, userDetails);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
