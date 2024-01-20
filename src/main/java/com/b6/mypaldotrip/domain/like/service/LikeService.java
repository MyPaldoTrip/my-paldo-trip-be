package com.b6.mypaldotrip.domain.like.service;

import com.b6.mypaldotrip.domain.course.service.CourseService;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.like.controller.dto.response.LikeCheckRes;
import com.b6.mypaldotrip.domain.like.controller.dto.response.LikeToggleRes;
import com.b6.mypaldotrip.domain.like.store.entity.LikeEntity;
import com.b6.mypaldotrip.domain.like.store.repository.LikeRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final CourseService courseService;
    private final VersionConfig versionConfig;

    public ResponseEntity<RestResponse<LikeToggleRes>> toggleLike(Long courseId, UserEntity user) {
        CourseEntity course = courseService.findCourse(courseId);
        Optional<LikeEntity> like = likeRepository.findByUserEntityAndCourseEntity(user, course);

        if (like.isPresent()) {
            likeRepository.delete(like.get());

            LikeToggleRes res = LikeToggleRes.builder().msg("좋아요 취소").build();
            return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                    .toResponseEntity();
        } else {
            LikeEntity newLike = LikeEntity.builder().user(user).course(course).build();
            likeRepository.save(newLike);

            LikeToggleRes res = LikeToggleRes.builder().msg("좋아요").build();
            return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                    .toResponseEntity();
        }
    }

    public ResponseEntity<RestResponse<LikeCheckRes>> likeCheck(Long courseId, UserEntity user) {
        CourseEntity course = courseService.findCourse(courseId);
        Optional<LikeEntity> like = likeRepository.findByUserEntityAndCourseEntity(user, course);

        if (like.isPresent()) {
            LikeCheckRes res = LikeCheckRes.builder().isLiked(true).build();
            return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                    .toResponseEntity();
        } else {
            LikeCheckRes res = LikeCheckRes.builder().isLiked(false).build();
            return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                    .toResponseEntity();
        }
    }
}
