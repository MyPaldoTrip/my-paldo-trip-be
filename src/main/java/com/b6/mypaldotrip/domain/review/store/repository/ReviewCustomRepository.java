package com.b6.mypaldotrip.domain.review.store.repository;

import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewSort;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCustomRepository {
    List<ReviewEntity> findByTripId(Long tripId, ReviewSort reviewSort, Pageable pageable);
}
