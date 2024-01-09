package com.b6.mypaldotrip.domain.trip.store.repository;

import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<TripEntity, Long> {
    Optional<TripEntity> findByName(String name);
}
