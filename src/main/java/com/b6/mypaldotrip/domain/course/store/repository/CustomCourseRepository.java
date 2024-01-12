package com.b6.mypaldotrip.domain.course.store.repository;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCourseRepository {
    List<CourseEntity> getCourseListByDynamicConditions(Pageable pageable, CourseSort courseSort, CourseSearchReq req);
}
