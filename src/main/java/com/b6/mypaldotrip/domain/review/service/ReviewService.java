package com.b6.mypaldotrip.domain.review.service;

import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewCreateReq;
import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewListReq;
import com.b6.mypaldotrip.domain.review.controller.dto.request.ReviewUpdateReq;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewCreateRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewDeleteRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewListRes;
import com.b6.mypaldotrip.domain.review.controller.dto.response.ReviewUpdateRes;
import com.b6.mypaldotrip.domain.review.exception.ReviewErrorCode;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.review.store.repository.ReviewRepository;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TripService tripService;

    public ReviewCreateRes createReview(Long tripId, ReviewCreateReq req) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review =
                ReviewEntity.builder().content(req.content()).score(req.score()).trip(trip).build();
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
                .filter(review -> review.getTrip().getTripId().equals(trip.getTripId()))
                .map(
                        review ->
                                ReviewListRes.builder()
                                        .content(review.getContent())
                                        .score(review.getScore())
                                        .modifiedAt(review.getModifiedAt())
                                        .build())
                .toList();
    }

    @Transactional
    public ReviewUpdateRes updateReview(Long tripId, Long reviewId, ReviewUpdateReq req) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review = findReview(reviewId);
        matchReviewAndTrip(review, trip);
        review.updateReview(req.content(), req.score());
        return ReviewUpdateRes.builder()
                .content(review.getContent())
                .score(review.getScore())
                .modifiedAt(review.getModifiedAt())
                .build();
    }

    public ReviewDeleteRes deleteReview(Long tripId, Long reviewId) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review = findReview(reviewId);
        matchReviewAndTrip(review, trip);
        reviewRepository.delete(review);
        return ReviewDeleteRes.builder().message("리뷰가 삭제되었습니다.").build();
    }

    // 해당 리뷰가 위치하는 여행정보가 맞는지 검증
    private static void matchReviewAndTrip(ReviewEntity review, TripEntity trip) {
        if (!review.getTrip().getTripId().equals(trip.getTripId())) {
            throw new GlobalException(ReviewErrorCode.MISMATCHED_TRIP_REVIEW);
        }
    }

    // 리뷰 조회 메서드
    public ReviewEntity findReview(Long reviewId) {
        return reviewRepository
                .findById(reviewId)
                .orElseThrow(() -> new GlobalException(ReviewErrorCode.NON_EXIST_REVIEW));
    }
}