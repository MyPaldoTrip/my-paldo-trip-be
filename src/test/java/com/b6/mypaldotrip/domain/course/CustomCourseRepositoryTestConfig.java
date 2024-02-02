package com.b6.mypaldotrip.domain.course;

import com.b6.mypaldotrip.domain.course.store.repository.CustomCourseRepository;
import com.b6.mypaldotrip.domain.course.store.repository.CustomCourseRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomCourseRepositoryTestConfig {

    @PersistenceContext private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public CustomCourseRepository courseCustomRepository() {
        return new CustomCourseRepositoryImpl(jpaQueryFactory());
    }
}
