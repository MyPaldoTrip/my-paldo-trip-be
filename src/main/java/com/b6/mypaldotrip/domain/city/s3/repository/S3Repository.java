package com.b6.mypaldotrip.domain.city.s3.repository;

import com.b6.mypaldotrip.domain.city.s3.dto.S3ListRes;
import com.b6.mypaldotrip.domain.city.s3.entity.S3Entity;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3Repository extends JpaRepository<S3Entity, Long> {
    List<S3ListRes> findAllByCityEntity(CityEntity city);
}
