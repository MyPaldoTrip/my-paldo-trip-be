package com.b6.mypaldotrip.domain.city.s3.entity;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_city_file")
public class CityFileEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileURL;

    @Builder
    private CityFileEntity(String fileUrl, CityEntity cityEntity) {
        this.fileURL = fileUrl;
        this.cityEntity = cityEntity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityEntity cityEntity;
}
