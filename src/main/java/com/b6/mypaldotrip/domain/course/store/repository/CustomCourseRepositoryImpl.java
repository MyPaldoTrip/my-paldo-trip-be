package com.b6.mypaldotrip.domain.course.store.repository;

import static com.b6.mypaldotrip.domain.course.store.entity.QCourseEntity.courseEntity;

import com.b6.mypaldotrip.domain.course.controller.dto.request.CourseSearchReq;
import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
import com.b6.mypaldotrip.domain.course.store.entity.CourseSort;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCourseRepositoryImpl implements CustomCourseRepository {

    private final JPAQueryFactory jpaQueryFactory;


    public Page<CourseEntity> getCourseListByDynamicConditions(
            Pageable pageable,
            CourseSort courseSort,
            CourseSearchReq req,
            Long userId,
            Boolean filterByFollowing) {

        List<CourseEntity> content =  jpaQueryFactory
                .selectFrom(courseEntity)
                .where(cityNameEq(req.filterByCityName()), isFollowing(userId, filterByFollowing))
                .leftJoin(courseEntity.userEntity)
                .fetchJoin()
                .orderBy(courseSort(courseSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(courseEntity.count())
            .from(courseEntity)
            .where(
                cityNameEq(req.filterByCityName()),
                isFollowing(userId, filterByFollowing)
            );

        return PageableExecutionUtils.getPage(content,pageable, countQuery::fetchOne);

    }
    @Override
    public void fetchComments(Long userId, CourseSearchReq req, Boolean filterByFollowing) {

        jpaQueryFactory
            .selectFrom(courseEntity)
            .leftJoin(courseEntity.comments)
            .fetchJoin()
            .where(
                cityNameEq(req.filterByCityName()), isFollowing(userId, filterByFollowing))
            .fetch();
    }@Override
    public void fetchLikes(Long userId, CourseSearchReq req, Boolean filterByFollowing) {

        jpaQueryFactory
            .selectFrom(courseEntity)
            .leftJoin(courseEntity.likes)
            .fetchJoin()
            .where(
                cityNameEq(req.filterByCityName()), isFollowing(userId, filterByFollowing))
            .fetch();
    }

    private BooleanExpression cityNameEq(String cityName) {
        return !Objects.equals(cityName, "") ? courseEntity.cityEntity.cityName.eq(cityName) : null;
    }

    private BooleanExpression isFollowing(Long userId, Boolean filterByFollowing) {
        return (userId != -1 && filterByFollowing)
                ? courseEntity.userEntity.followerList.any().followedUser.userId.eq(userId)
                : null;
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
