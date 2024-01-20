package com.b6.mypaldotrip.domain.trip.store.repository;

import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.trip.store.entity.QTripEntity;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.entity.TripSort;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TripRepositoryImpl implements TripCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    QTripEntity trip = QTripEntity.tripEntity;

    @Override
    public List<TripEntity> searchTripsAndSort(
            String cityName, Category category, TripSort tripSort, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(trip)
                .join(trip.city)
                .fetchJoin()
                .leftJoin(trip.reviewList)
                .fetchJoin()
                .where(getEqCityName(trip, cityName), getEqCategory(trip, category))
                .orderBy(checkTripSort(tripSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression getEqCityName(QTripEntity trip, String cityName) {
        return StringUtils.hasText(cityName) ? trip.city.cityName.contains(cityName) : null;
    }

    private BooleanExpression getEqCategory(QTripEntity trip, Category category) {
        return category != null ? trip.category.eq(category) : null;
    }

    private OrderSpecifier<?> checkTripSort(TripSort tripSort) {
        switch (tripSort) {
            case CREATED -> {
                return trip.createdAt.desc();
            }
            case RATING -> {
                return trip.averageRating.desc();
            }
            case REVIEWS -> {
                return trip.reviewList.size().desc();
            }
            default -> throw new GlobalException(TripErrorCode.WRONG_TRIP_SORT);
        }
    }
}
