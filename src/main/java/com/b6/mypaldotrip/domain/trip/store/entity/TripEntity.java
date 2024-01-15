package com.b6.mypaldotrip.domain.trip.store.entity;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_trip")
public class TripEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private String description;

    private double averageRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity city;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviewList = new ArrayList<>();

    @Builder
    private TripEntity(CityEntity city, Category category, String name, String description) {
        this.city = city;
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public void updateTrip(CityEntity city, Category category, String name, String description) {
        this.city = city == null ? this.city : city;
        this.category = category == null ? this.category : category;
        this.name = name == null ? this.name : name;
        this.description = description == null ? this.description : description;
    }

    public void calculateAverageRating() {
        double totalScore = 0;
        for (ReviewEntity review : reviewList) {
            totalScore += review.getScore();
        }
        this.averageRating = (double) Math.round((totalScore / reviewList.size()) * 10) / 10;
    }
}
