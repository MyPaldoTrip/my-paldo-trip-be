package com.b6.mypaldotrip.domain.trip;

import com.b6.mypaldotrip.domain.user.store.repository.UserCustomRepository;
import com.b6.mypaldotrip.domain.user.store.repository.UserCustomRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TripCustomRepositoryTestConfig {

    @PersistenceContext private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public UserCustomRepository userCustomRepository() {
        return new UserCustomRepositoryImpl(jpaQueryFactory());
    }
}
