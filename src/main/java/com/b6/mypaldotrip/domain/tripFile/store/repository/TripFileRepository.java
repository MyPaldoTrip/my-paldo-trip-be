package com.b6.mypaldotrip.domain.tripFile.store.repository;

import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripFileRepository extends JpaRepository<TripFileEntity, Long> {
    TripFileEntity findByTrip_TripIdAndTripFileId(Long tripId, Long tripFileId);
}
