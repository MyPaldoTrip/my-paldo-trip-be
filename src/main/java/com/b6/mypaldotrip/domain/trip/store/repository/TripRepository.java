package com.b6.mypaldotrip.domain.trip.store.repository;

import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<TripEntity, Long>, TripRepositoryCustom {
    Optional<TripEntity> findByName(String name);
}
