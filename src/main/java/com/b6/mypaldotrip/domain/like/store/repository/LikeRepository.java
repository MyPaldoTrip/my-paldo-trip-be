package com.b6.mypaldotrip.domain.like.store.repository;

import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.like.store.entity.LikeEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserEntityAndCourseEntity(UserEntity user, CourseEntity course);
}
