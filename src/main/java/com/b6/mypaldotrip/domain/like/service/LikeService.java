package com.b6.mypaldotrip.domain.like.service;

import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.like.controller.dto.response.LikeToggleRes;
import com.b6.mypaldotrip.domain.like.store.entity.LikeEntity;
import com.b6.mypaldotrip.domain.like.store.repository.LikeRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CourseService courseService;

    public LikeToggleRes toggleLike(Long courseId, UserEntity user) {
        CourseEntity course = courseService.findCourse(courseId);
        Optional<LikeEntity> like = likeRepository.findByUserEntityAndCourseEntity(user, course);

        if (like.isPresent()) {
            likeRepository.delete(like.get());

            return LikeToggleRes.builder().msg("좋아요 취소").build();
        } else {
            LikeEntity newLike = LikeEntity.builder().user(user).course(course).build();
            likeRepository.save(newLike);

            return LikeToggleRes.builder().msg("좋아요").build();
        }
    }
}
