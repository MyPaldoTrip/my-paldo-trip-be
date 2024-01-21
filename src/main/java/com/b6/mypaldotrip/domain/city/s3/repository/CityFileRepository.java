package com.b6.mypaldotrip.domain.city.s3.repository;

import com.b6.mypaldotrip.domain.city.s3.dto.CItyFileListRes;
import com.b6.mypaldotrip.domain.city.s3.entity.CityFileEntity;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityFileRepository extends JpaRepository<CityFileEntity, Long> {
    List<CItyFileListRes> findAllByCityEntity(CityEntity city);
}
