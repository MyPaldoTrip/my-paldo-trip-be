package com.b6.mypaldotrip.domain.comment.service;

import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSaveReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSearchReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentListRes;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentSaveRes;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentSort;
import com.b6.mypaldotrip.domain.comment.store.repository.CommentRepository;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.List;
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
            Long courseId, int page, int size, CommentSearchReq req, UserEntity userEntity) {
        Pageable pageable = PageRequest.of(page, size);
        CommentSort commentSort =
                req.commentSort() != null ? req.commentSort() : CommentSort.MODIFIED;

        List<CommentListRes> res =
                commentRepository
                        .getCommentListByDynamicConditions(
                                courseId, pageable, commentSort, userEntity)
                        .stream()
                        .map(c -> CommentListRes.builder().content(c.getContent()).build())
                        .toList();

        return res;
    }
}
