package com.b6.mypaldotrip.domain.trip.store.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.trip.TripTest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TripEntityTest implements TripTest {

    @Test
    @DisplayName("여행정보 업데이트 테스트")
    public void 여행정보_업데이트() {
        // given
        CityEntity newCity = TEST_CITY;
        Category newCategory = Category.RESTAURANT;
        String newName = "new name";
        String newDescription = "new description";

        // when
        TEST_ANOTHER_TRIP.updateTrip(newCity, newCategory, newName, newDescription);

        // then
        assertThat(TEST_ANOTHER_TRIP.getCity().getCityName()).isEqualTo(newCity.getCityName());
        assertThat(TEST_ANOTHER_TRIP.getCategory()).isEqualTo(newCategory);
        assertThat(TEST_ANOTHER_TRIP.getName()).isEqualTo(newName);
        assertThat(TEST_ANOTHER_TRIP.getDescription()).isEqualTo(newDescription);
    }

    @Test
    @DisplayName("평균 별점 계산 테스트")
    public void 평균별점계산() {
        // given
        List<ReviewEntity> reviews =
                Arrays.asList(
                        ReviewEntity.builder().score(1).build(),
                        ReviewEntity.builder().score(2).build(),
                        ReviewEntity.builder().score(3).build(),
                        ReviewEntity.builder().score(4).build(),
                        ReviewEntity.builder().score(5).build());
        TripEntity trip =
                TripEntity.builder()
                        .city(TEST_CITY)
                        .category(Category.ATTRACTION)
                        .name(TEST_TRIP_NAME)
                        .description(TEST_DESCRIPTION)
                        .build();
        trip.getReviewList().addAll(reviews);

        // when
        trip.calculateAverageRating();

        // then
        assertThat(trip.getAverageRating()).isEqualTo(3.0);
    }
}
