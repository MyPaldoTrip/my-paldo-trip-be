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
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TripService tripService;

    public ReviewCreateRes createReview(
            Long tripId, ReviewCreateReq req, UserDetailsImpl userDetails) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review =
                ReviewEntity.builder()
                        .user(userDetails.getUserEntity())
                        .content(req.content())
                        .score(req.score())
                        .trip(trip)
                        .build();
        reviewRepository.save(review);
        return ReviewCreateRes.builder()
                .username(userDetails.getUsername())
                .content(review.getContent())
                .score(review.getScore())
                .modifiedAt(review.getModifiedAt())
                .build();
    }

    public List<ReviewListRes> getReviewList(Long tripId, ReviewListReq req) {
        tripService.findTrip(tripId);
        return reviewRepository.findByTripId(tripId);
    }

    @Transactional
    public ReviewUpdateRes updateReview(
            Long tripId, Long reviewId, ReviewUpdateReq req, UserDetailsImpl userDetails) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review = findReview(reviewId);

        matchReviewAndTrip(review, trip);
        checkAuthor(userDetails, review);
        review.updateReview(req.content(), req.score());

        return ReviewUpdateRes.builder()
                .username(review.getUser().getUsername())
                .content(review.getContent())
                .score(review.getScore())
                .modifiedAt(review.getModifiedAt())
                .build();
    }

    public ReviewDeleteRes deleteReview(Long tripId, Long reviewId, UserDetailsImpl userDetails) {
        TripEntity trip = tripService.findTrip(tripId);
        ReviewEntity review = findReview(reviewId);
        matchReviewAndTrip(review, trip);
        checkAuthor(userDetails, review);
        reviewRepository.delete(review);
        return ReviewDeleteRes.builder().message("리뷰가 삭제되었습니다.").build();
    }

    // 작성자 검증
    private static void checkAuthor(UserDetailsImpl userDetails, ReviewEntity review) {
        if (!Objects.equals(
                review.getUser().getUserId(), userDetails.getUserEntity().getUserId())) {
            throw new GlobalException(ReviewErrorCode.NOT_AUTHOR_ERROR);
        }
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
