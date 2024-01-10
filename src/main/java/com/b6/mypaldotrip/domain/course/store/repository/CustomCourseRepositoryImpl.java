package com.b6.mypaldotrip.domain.course.store.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
