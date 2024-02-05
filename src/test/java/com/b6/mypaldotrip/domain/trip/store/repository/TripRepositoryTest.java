package com.b6.mypaldotrip.domain.trip.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.domain.trip.TripCustomRepositoryTestConfig;
import com.b6.mypaldotrip.domain.trip.TripTest;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(TripCustomRepositoryTestConfig.class)
@ActiveProfiles("test")
public class TripRepositoryTest implements TripTest {

    @Autowired private TripRepository tripRepository;

    @Autowired private CityRepository cityRepository;

    @Test
    @DisplayName("여행정보 이름으로 찾기")
    public void 이름으로찾기() {
        // given
        cityRepository.save(TEST_CITY);
        tripRepository.save(TEST_TRIP);

        // when
        Optional<TripEntity> trip = tripRepository.findByName(TEST_TRIP_NAME);

        // then
        assertThat(trip).isPresent();
        assertThat(trip.get().getName()).isEqualTo(TEST_TRIP_NAME);
    }
}
