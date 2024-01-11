package com.b6.mypaldotrip.domain.review.store.entity;

import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_review")
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;

    private Integer score;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private TripEntity trip;

    @Builder
    private ReviewEntity(String content, Integer score, TripEntity trip) {
        this.content = content;
        this.score = score;
        this.trip = trip;
    }

    public void updateReview(String content, Integer score) {
        this.content = content == null ? this.content : content;
        this.score = score == null ? this.score : score;
    }
}
