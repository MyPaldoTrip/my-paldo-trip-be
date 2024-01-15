package com.b6.mypaldotrip.domain.city.store.repository;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.entity.CitySort;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CityCustomRepository {

    List<CityEntity> getProvinceSort(CitySort citySort);
}
