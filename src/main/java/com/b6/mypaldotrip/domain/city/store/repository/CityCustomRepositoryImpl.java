package com.b6.mypaldotrip.domain.city.store.repository;

import static com.b6.mypaldotrip.domain.city.store.entity.QCityEntity.cityEntity;

import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.entity.CitySort;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CityCustomRepositoryImpl implements CityCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CityEntity> getProvinceSort(CitySort citySort) {

        return jpaQueryFactory
                .selectFrom(cityEntity)
                .leftJoin(cityEntity.tripEntityList)
                .fetchJoin()
                .groupBy(cityEntity.provinceName)
                .orderBy(checkCitySort(citySort))
                .fetch();
    }

    public OrderSpecifier<?> checkCitySort(CitySort citySort) {
        switch (citySort) {
            case INITIAL -> {
                return cityEntity.cityId.desc();
            }
            case COUNT -> {
                return cityEntity.tripEntityList.size().desc();
            }

            default -> throw new GlobalException(CityErrorCode.WRONG_CITY_SORT);
        }
    }
}
