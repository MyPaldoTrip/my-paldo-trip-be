package com.b6.mypaldotrip.domain.review.controller;

import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewCreateReq;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewCreateRes;
import com.b6.mypaldotrip.domain.review.service.ReviewService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/${mpt.version}/trips/{tripId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<ReviewCreateRes>> createReview(
            @PathVariable Long tripId, @Valid @RequestBody ReviewCreateReq req) {
        ReviewCreateRes res = reviewService.createReview(tripId, req);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }
}
