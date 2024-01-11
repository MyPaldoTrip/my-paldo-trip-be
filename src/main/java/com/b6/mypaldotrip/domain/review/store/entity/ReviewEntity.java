package com.b6.mypaldotrip.domain.review.store.entity;

import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tq_review")
public class ReviewEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;

    private Integer score;

    @Builder
    private ReviewEntity(String content, Integer score) {
        this.content = content;
        this.score = score;
    }
}
