package com.b6.mypaldotrip.domain.comment.service;

import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSaveReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSearchReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentUpdateReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentDeleteRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentGetRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentListRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentSaveRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentUpdateRes;
import com.b6.mypaldotrip.domain.comment.exception.CommentErrorCode;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentSort;
import com.b6.mypaldotrip.domain.comment.store.repository.CommentRepository;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.exception.GlobalException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CourseService courseService;

    public CommentSaveRes saveComment(Long courseId, CommentSaveReq req, UserEntity user) {
        CourseEntity course = courseService.findCourse(courseId);

        CommentEntity comment =
                CommentEntity.builder()
                        .content(req.content())
                        .userEntity(user)
                        .courseEntity(course)
                        .build();

        commentRepository.save(comment);

        return CommentSaveRes.builder().content(comment.getContent()).build();
    }

    public List<CommentListRes> getCommentListByDynamicConditions(
            Long courseId, int page, int size, CommentSearchReq req, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        CommentSort commentSort =
                req.commentSort() != null ? req.commentSort() : CommentSort.MODIFIED;
        Boolean filterByFollowing = req.filterByFollowing();
        int totalPages =
                commentRepository
                        .getCommentListByDynamicConditions(
                                courseId, pageable, commentSort, userId, filterByFollowing)
                        .getTotalPages();

        List<CommentListRes> res =
                commentRepository
                        .getCommentListByDynamicConditions(
                                courseId, pageable, commentSort, userId, filterByFollowing)
                        .stream()
                        .map(
                                c ->
                                        CommentListRes.builder()
                                                .commentId(c.getCommentId())
                                                .username(c.getUserEntity().getUsername())
                                                .content(c.getContent())
                                                .level(c.getUserEntity().getLevel())
                                                .modifiedAt(c.getModifiedAt())
                                                .totalPage(totalPages)
                                                .build())
                        .toList();

        return res;
    }

    public CommentGetRes getComment(Long courseId, Long commentId) {
        courseService.findCourse(courseId);

        CommentEntity commentEntity = findComment(commentId);

        CommentGetRes res = CommentGetRes.builder().content(commentEntity.getContent()).build();

        return res;
    }

    @Transactional
    public CommentUpdateRes updateComment(
            Long courseId, Long commentId, CommentUpdateReq req, UserEntity userEntity) {
        courseService.findCourse(courseId);

        CommentEntity commentEntity = findComment(commentId);

        validateAuth(userEntity, commentEntity);

        commentEntity.updateComment(req.content());

        CommentUpdateRes res =
                CommentUpdateRes.builder().content(commentEntity.getContent()).build();

        return res;
    }

    public CommentDeleteRes deleteComment(Long courseId, Long commentId, UserEntity userEntity) {
        courseService.findCourse(courseId);

        CommentEntity commentEntity = findComment(commentId);

        validateAuth(userEntity, commentEntity);

        commentRepository.delete(commentEntity);

        CommentDeleteRes res = CommentDeleteRes.builder().msg(commentId + "번 댓글 삭제완료").build();

        return res;
    }

    private CommentEntity findComment(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new GlobalException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    private static void validateAuth(UserEntity userEntity, CommentEntity commentEntity) {
        if (userEntity.getUserRole() == UserRole.ROLE_USER
                && !Objects.equals(
                        userEntity.getUserId(), commentEntity.getUserEntity().getUserId())) {
            throw new GlobalException(CommentErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
