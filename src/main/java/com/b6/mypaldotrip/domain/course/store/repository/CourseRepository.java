package com.b6.mypaldotrip.domain.course.store.repository;

import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

}
