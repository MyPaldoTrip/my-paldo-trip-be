package com.b6.mypaldotrip.domain.course.store.repository;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCourseRepository {
    Page<CourseEntity> getCourseListByDynamicConditions(
            Pageable pageable,
            CourseSort courseSort,
            CourseSearchReq req,
            Long userId,
            Boolean filterByFollowing);

    void fetchComments(Long userId, CourseSearchReq req, Boolean filterByFollowing);

    void fetchLikes(Long userId, CourseSearchReq req, Boolean filterByFollowing);
}


