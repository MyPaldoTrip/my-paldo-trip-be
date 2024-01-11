package com.b6.mypaldotrip.domain.review.service;

import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewCreateReq;
import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewListReq;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewCreateRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.review.store.repository.ReviewRepository;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TripService tripService;

    public ReviewCreateRes createReview(Long tripId, ReviewCreateReq req) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review =
                ReviewEntity.builder().content(req.content()).score(req.score()).build();
        reviewRepository.save(review);
        // TODO: 2024-01-11 response에 유저 정보 추가 필요
        return ReviewCreateRes.builder()
                .content(review.getContent())
                .score(review.getScore())
                .modifiedAt(review.getModifiedAt())
                .build();
    }

    public List<ReviewListRes> getReviewList(Long tripId, ReviewListReq req) {
        TripEntity trip = tripService.findTrip(tripId);
        return reviewRepository.findAll().stream()
                .map(
                        review ->
                                ReviewListRes.builder()
                                        .content(review.getContent())
                                        .score(review.getScore())
                                        .modifiedAt(review.getModifiedAt())
                                        .build())
                .toList();
    }
}
