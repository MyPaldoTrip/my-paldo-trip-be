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
public class TripRepositoryImpl implements TripCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<TripEntity> searchTripsAndSort(
            String cityName, Category category, Pageable pageable) {

        QTripEntity trip = QTripEntity.tripEntity;
        return jpaQueryFactory
                .selectFrom(trip)
                .join(trip.city)
                .fetchJoin()
                .where(getEqCityName(trip, cityName), getEqCategory(trip, category))
                .orderBy(trip.tripId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression getEqCityName(QTripEntity trip, String cityName) {
        return StringUtils.hasText(cityName) ? trip.city.cityName.eq(cityName) : null;
    }

    private BooleanExpression getEqCategory(QTripEntity trip, Category category) {
        return category != null ? trip.category.eq(category) : null;
    }
}
