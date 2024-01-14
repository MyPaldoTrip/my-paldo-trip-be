package com.b6.mypaldotrip.domain.comment.controller;

import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSaveReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSearchReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentUpdateReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentDeleteRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentGetRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentListRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentSaveRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentUpdateRes;
import com.b6.mypaldotrip.domain.comment.service.CommentService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        CommentSaveRes res = commentService.saveComment(courseId, req, userDetails.getUserEntity());

        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<CommentListRes>>> getCommentListByDynamicConditions(
            @PathVariable Long courseId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestBody CommentSearchReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails != null ? userDetails.getUserEntity().getUserId() : null;
        List<CommentListRes> res =
                commentService.getCommentListByDynamicConditions(courseId, page, size, req, userId);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<RestResponse<CommentGetRes>> getComment(
            @PathVariable Long courseId, @PathVariable Long commentId) {
        CommentGetRes res = commentService.getComment(courseId, commentId);

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<RestResponse<CommentUpdateRes>> updateComment(
            @PathVariable Long courseId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentUpdateRes res =
                commentService.updateComment(courseId, commentId, req, userDetails.getUserEntity());

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<RestResponse<CommentDeleteRes>> deleteComment(
            @PathVariable Long courseId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CommentDeleteRes res =
                commentService.deleteComment(courseId, commentId, userDetails.getUserEntity());

        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
