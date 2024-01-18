package com.b6.mypaldotrip.domain.user.store.repository;

import static com.b6.mypaldotrip.domain.follow.store.entity.QFollowerEntity.followerEntity;
import static com.b6.mypaldotrip.domain.follow.store.entity.QFollowingEntity.followingEntity;
import static com.b6.mypaldotrip.domain.user.store.entity.QUserEntity.userEntity;

import com.b6.mypaldotrip.domain.user.controller.dto.request.UserListReq;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.domain.user.store.entity.UserSort;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserEntity> findByDynamicConditions(UserListReq req, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(req.page(), req.size());

        return jpaQueryFactory
                .selectFrom(userEntity)
                .leftJoin(userEntity.reviewList)
                .fetchJoin()
                .where(
                        ageEq(req.ageCondition()),
                        levelEq(req.levelCondition()),
                        userRoleEq(req.userRoleCondition()),
                        existFollowerCondition(req.followerCondition(), userDetails),
                        existFollowingCondition(req.followingCondition(), userDetails))
                .orderBy(sortCondition(req.userSort(), req.asc()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public void fetchFollowerList(UserListReq req, UserDetailsImpl userDetails) {

        jpaQueryFactory
            .selectFrom(userEntity)
            .leftJoin(userEntity.followerList)
            .fetchJoin()
            .where(
                ageEq(req.ageCondition()),
                levelEq(req.levelCondition()),
                userRoleEq(req.userRoleCondition()),
                existFollowerCondition(req.followerCondition(), userDetails),
                existFollowingCondition(req.followingCondition(), userDetails))
            .fetch();
    }

    private OrderSpecifier<?> sortCondition(UserSort userSort, Boolean isAsc) {
        userSort = userSort != null ? userSort : UserSort.MODIFIED;
        Order order = isAsc != null ? Order.ASC : Order.DESC;
        switch (userSort) {
            case AGE -> {
                return new OrderSpecifier<>(order, userEntity.age);
            }
            case LEVEL -> {
                return new OrderSpecifier<>(order, userEntity.level);
            }
            case WRITE_REVIEW_CNT -> {
                return new OrderSpecifier<>(order, userEntity.reviewList.size());
            }
            case FOLLOWER_CNT -> {
                return new OrderSpecifier<>(order, userEntity.followerList.size());
            }
            default -> {
                return new OrderSpecifier<>(order, userEntity.modifiedAt);
            }
        }
    }

    private BooleanExpression ageEq(Long ageCondition) {
        return ageCondition != null ? userEntity.age.eq(ageCondition) : null;
    }

    private BooleanExpression levelEq(Long levelCondition) {
        return levelCondition != null ? userEntity.level.eq(levelCondition) : null;
    }

    private BooleanExpression userRoleEq(UserRole userRole) {
        return userRole != null ? userEntity.userRole.eq(userRole) : null;
    }

    private BooleanExpression existFollowerCondition(
            Boolean followerCondition, UserDetailsImpl userDetails) {
        if (userDetails == null) return null;
        UserEntity loggedUser = userDetails.getUserEntity();
        return followerCondition != null
                ? userEntity.eqAny(
                        JPAExpressions.select(followerEntity.followedUser)
                                .from(followerEntity)
                                .where(followerEntity.user.eq(loggedUser)))
                : null;
    }

    private BooleanExpression existFollowingCondition(
            Boolean followingCondition, UserDetailsImpl userDetails) {
        if (userDetails == null) return null;
        UserEntity loggedUser = userDetails.getUserEntity();
        return followingCondition != null
                ? userEntity.eqAny(
                        JPAExpressions.select(followingEntity.followingUser)
                                .from(followingEntity)
                                .where(followingEntity.user.eq(loggedUser)))
                : null;
    }
}
