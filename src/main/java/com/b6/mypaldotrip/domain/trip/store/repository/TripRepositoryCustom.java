package com.b6.mypaldotrip.domain.trip.store.repository;

import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepositoryCustom {
    List<TripEntity> searchTripsAndSort(String cityName, Category category, Pageable pageable);
}
