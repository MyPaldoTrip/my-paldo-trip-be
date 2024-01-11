package com.b6.mypaldotrip.domain.review.store.repository;

import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    @Query(
            "select new com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes(r.user.username, r.content, r.score, r.modifiedAt) "
                    + "from ReviewEntity r where r.trip.tripId = :tripId")
    List<ReviewListRes> findByTripId(@Param("tripId") Long tripId);
}
