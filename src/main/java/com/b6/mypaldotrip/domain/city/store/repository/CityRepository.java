package com.b6.mypaldotrip.domain.city.store.repository;

import com.b6.mypaldotrip.domain.city.store.entity.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByCityName(String cityName);
}
