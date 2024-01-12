package com.b6.mypaldotrip.domain.course.store.repository;

import static com.b6.mypaldotrip.domain.course.store.entity.QCourseEntity.courseEntity;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CourseEntity> getCourseListByDynamicConditions(
            Pageable pageable, CourseSort courseSort, CourseSearchReq req) {

        return jpaQueryFactory
                .selectFrom(courseEntity)
                .where(cityNameEq(req.cityName()), following(req.username()))
                .leftJoin(courseEntity.cityEntity)
                .orderBy(courseSort(courseSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression cityNameEq(String cityName) {
        return cityName != null ? courseEntity.cityEntity.cityName.eq(cityName) : null;
    }

    private BooleanExpression following(String username) {
        return null;
    }

    public OrderSpecifier<?> courseSort(CourseSort courseSort) {
        switch (courseSort) {
            case MODIFIED -> {
                return courseEntity.modifiedAt.desc();
            }
            case LEVEL -> {
                return courseEntity.userEntity.level.desc();
            }
            case LIKE -> {
                return courseEntity.likes.size().desc();
            }
            case COMMENT -> {
                return courseEntity.comments.size().desc();
            }
            default -> throw new GlobalException(CourseErrorCode.WRONG_COURSE_SORT);
        }
    }
}
