package com.b6.mypaldotrip.domain.review.store.repository;

import com.b6.mypaldotrip.domain.follow.store.entity.QFollowingEntity;
import com.b6.mypaldotrip.domain.review.exception.ReviewErrorCode;
import com.b6.mypaldotrip.domain.review.store.entity.QReviewEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewSort;
import com.b6.mypaldotrip.domain.trip.store.entity.QTripEntity;
import com.b6.mypaldotrip.domain.user.store.entity.QUserEntity;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QTripEntity trip = QTripEntity.tripEntity;
    QFollowingEntity following = QFollowingEntity.followingEntity;

    @Override
    public List<ReviewEntity> findByTripIdAndSort(
            Long tripId,
            Long userId,
            boolean isFollowingOnly,
            ReviewSort reviewSort,
            Pageable pageable) {
        BooleanExpression eqTripId = review.trip.tripId.eq(tripId);
        BooleanExpression getFollowingUser = findFollowingUserIdList(userId, isFollowingOnly);

        return jpaQueryFactory
                .selectFrom(review)
                .join(review.user, user)
                .fetchJoin()
                .join(review.trip, trip)
                .where(eqTripId, getFollowingUser)
                .orderBy(checkReviewSort(reviewSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression findFollowingUserIdList(Long userId, boolean isFollowingOnly) {
        BooleanExpression getFollowingUser = null;
        if (isFollowingOnly && userId != null) {
            getFollowingUser =
                    review.user.userId.in(
                            JPAExpressions.select(following.followingUser.userId)
                                    .from(following)
                                    .where(following.user.userId.eq(userId)));
        }
        return getFollowingUser;
    }

    private OrderSpecifier<?> checkReviewSort(ReviewSort reviewSort) {
        switch (reviewSort) {
            case MODIFIED -> {
                return review.modifiedAt.desc();
            }
            case RATING -> {
                return review.score.desc();
            }
            case LEVEL -> {
                return user.level.desc();
            }
            default -> throw new GlobalException(ReviewErrorCode.WRONG_REVIEW_SORT);
        }
    }
}
