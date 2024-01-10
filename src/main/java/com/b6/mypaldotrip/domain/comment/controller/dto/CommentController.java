package com.b6.mypaldotrip.domain.comment.controller.dto;

import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSaveReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentSaveRes;
import com.b6.mypaldotrip.domain.comment.service.CommentService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${mpt.version}/courses/{courseId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<CommentSaveRes>> saveComment(
            @PathVariable Long courseId,
            @RequestBody CommentSaveReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentSaveRes res = commentService.saveComment(courseId, req, userDetails.getUser());

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
