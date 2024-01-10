package com.b6.mypaldotrip.domain.trip.store.repository;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.trip.store.entity.QTripEntity;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class TripRepositoryImpl implements TripRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TripEntity> searchTripsAndSort(
            String cityName, Category category, Pageable pageable) {
        QTripEntity trip = QTripEntity.tripEntity;
        BooleanExpression predicate = trip.isNotNull();

        if (StringUtils.hasText(cityName)) {
            predicate = predicate.and(trip.city.cityName.eq(cityName));
        }

        if (category != null) {
            predicate = predicate.and(trip.category.eq(category));
        }

        return jpaQueryFactory
                .selectFrom(trip)
                .join(trip.city)
                .fetchJoin()
                .where(predicate)
                .orderBy(trip.tripId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
