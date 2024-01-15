package com.b6.mypaldotrip.domain.courseFile.store.repository;

import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.courseFile.store.entity.CourseFileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseFileRepository extends JpaRepository<CourseFileEntity, Long> {
    List<CourseFileEntity> findAllByCourseEntity(CourseEntity courseEntity);
}
