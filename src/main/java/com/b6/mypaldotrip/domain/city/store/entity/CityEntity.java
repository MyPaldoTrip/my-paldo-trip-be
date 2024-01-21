package com.b6.mypaldotrip.domain.city.store.entity;

import com.b6.mypaldotrip.domain.city.s3.entity.CityFileEntity;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_city")
@Entity
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @Column(nullable = false)
    private String provinceName;

    @Column(nullable = false, unique = true)
    private String cityName;

    private String cityInfo;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TripEntity> tripEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "cityEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CityFileEntity> files = new ArrayList<>();

    @Builder
    public CityEntity(final String provinceName, final String cityName, final String cityInfo) {
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.cityInfo = cityInfo;
    }

    public void update(String provinceName, String cityName, String cityInfo) {
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.cityInfo = cityInfo;
    }
}
