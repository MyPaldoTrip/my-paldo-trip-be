package com.b6.mypaldotrip.domain.comment.service;

import com.b6.mypaldotrip.domain.comment.controller.dto.request.CommentSaveReq;
import com.b6.mypaldotrip.domain.comment.controller.dto.response.CommentSaveRes;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.repository.CommentRepository;
import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import lombok.RequiredArgsConstructor;
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
}
