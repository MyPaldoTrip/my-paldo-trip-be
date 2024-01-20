package com.b6.mypaldotrip.domain.tripFile.store.entity;

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
@Table(name = "tb_trip_file")
public class TripFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tripFileId;

    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private TripEntity trip;

    @Builder
    private TripFileEntity(Long tripfileId, TripEntity trip, String fileUrl) {
        this.tripFileId = tripfileId;
        this.fileUrl = fileUrl;
        this.trip = trip;
    }
}
