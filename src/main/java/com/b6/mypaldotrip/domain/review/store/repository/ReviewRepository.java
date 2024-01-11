package com.b6.mypaldotrip.domain.review.store.repository;

import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<List<ReviewEntity>> findByTrip_TripId(Long tripId);
}
