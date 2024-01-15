package com.b6.mypaldotrip.domain.city.store.repository;

import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long>, CityCustomRepository {

    Optional<CityEntity> findByCityName(String cityName);

    @Query("SELECT DISTINCT c.provinceName FROM CityEntity c")
    List<String> findDistinctByProvinceName();

    List<CityListRes> findByProvinceName(String provincesName);
}
