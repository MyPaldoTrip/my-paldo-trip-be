package com.b6.mypaldotrip.domain.review.store.repository;

import com.b6.mypaldotrip.domain.review.exception.ReviewErrorCode;
import com.b6.mypaldotrip.domain.review.store.entity.QReviewEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewSort;
import com.b6.mypaldotrip.domain.trip.store.entity.QTripEntity;
import com.b6.mypaldotrip.domain.user.store.entity.QUserEntity;
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
public class ReviewRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QReviewEntity review = QReviewEntity.reviewEntity;
    QUserEntity user = QUserEntity.userEntity;
    QTripEntity trip = QTripEntity.tripEntity;

    @Override
    public List<ReviewEntity> findByTripId(Long tripId, ReviewSort reviewSort, Pageable pageable) {
        BooleanExpression checkTripId = review.trip.tripId.eq(tripId);
        return jpaQueryFactory
                .selectFrom(review)
                .join(review.user, user)
                .fetchJoin()
                .join(review.trip, trip)
                .where(checkTripId)
                .orderBy(checkReviewSort(reviewSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public OrderSpecifier<?> checkReviewSort(ReviewSort reviewSort) {
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
