package com.b6.mypaldotrip.domain.trip.store.entity;

import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.*;
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

    // TODO: 2024-01-08 city와 연관관계 설정 필요

    @Builder
    private TripEntity(Category category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public void updateTrip(Category category, String name, String description) {
        this.category = category == null ? this.category : category;
        this.name = name == null ? this.name : name;
        this.description = description == null ? this.description : description;
    }
}
