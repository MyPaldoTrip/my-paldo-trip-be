package com.b6.mypaldotrip.domain.trip.store.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.domain.review.store.entity.ReviewEntity;
import com.b6.mypaldotrip.domain.review.store.repository.ReviewRepository;
import com.b6.mypaldotrip.domain.trip.TripCustomRepositoryTestConfig;
import com.b6.mypaldotrip.domain.trip.TripTest;
import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.entity.TripSort;
import com.b6.mypaldotrip.domain.user.store.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(TripCustomRepositoryTestConfig.class)
@ActiveProfiles("test")
public class TripCustomRepositoryTest implements TripTest {

    @Autowired private TripRepository tripRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;

    private TripEntity testTrip1;
    private TripEntity testTrip2;
    private TripEntity testTrip3;

    @BeforeEach
    void setUp() {
        cityRepository.save(TEST_CITY);
        userRepository.save(TEST_USER);
        testTrip1 = createTripEntity("Test Trip 1", "Test Description 1");
        addReviewsToTrip(testTrip1, 3, 5);
        testTrip2 = createTripEntity("Test Trip 2", "Test Description 2");
        addReviewsToTrip(testTrip2, 4, 10);
        testTrip3 = createTripEntity("Test Trip 3", "Test Description 3");
        addReviewsToTrip(testTrip3, 5, 15);
    }

    @Test
    @DisplayName("도시 이름으로 여행정보 조회")
    public void 도시이름으로_여행정보조회() {
        // given
        tripRepository.save(TEST_TRIP);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<TripEntity> trips =
                tripRepository.searchTripsAndSort(TEST_CITY_NAME, null, TripSort.CREATED, pageable);

        // then
        assertThat(trips).isNotEmpty();
        assertThat(trips.get(0).getCity().getCityName()).isEqualTo(TEST_CITY_NAME);
    }

    @Test
    @DisplayName("카테고리 별 여행정보 조회")
    public void 카테고리별_여행정보조회() {
        // given
        tripRepository.save(TEST_TRIP);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        List<TripEntity> trips =
                tripRepository.searchTripsAndSort(
                        null, Category.ATTRACTION, TripSort.CREATED, pageable);

        // then
        assertThat(trips).isNotEmpty();
        assertThat(trips.get(0).getCategory()).isEqualTo(Category.ATTRACTION);
    }

    @Nested
    @DisplayName("여행정보 동적조회 테스트")
    class 여행정보_동적조회 {

        @Test
        @DisplayName("여행정보 동적조회 테스트 - 생성 순")
        public void 여행정보_동적조회1() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            List<TripEntity> trips =
                    tripRepository.searchTripsAndSort(null, null, TripSort.CREATED, pageable);

            // then
            assertThat(trips).isNotEmpty();
            assertThat(trips.get(0).getName()).isEqualTo(testTrip1.getName());
            assertThat(trips.get(1).getName()).isEqualTo(testTrip2.getName());
            assertThat(trips.get(2).getName()).isEqualTo(testTrip3.getName());
        }

        @Test
        @DisplayName("여행정보 동적조회 테스트 - 평점 순")
        public void 여행정보_동적조회2() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            List<TripEntity> trips =
                    tripRepository.searchTripsAndSort(null, null, TripSort.RATING, pageable);

            // then
            assertThat(trips).isNotEmpty();
            assertThat(trips.get(0).getAverageRating()).isEqualTo(5.0);
            assertThat(trips.get(1).getAverageRating()).isEqualTo(4.0);
            assertThat(trips.get(2).getAverageRating()).isEqualTo(3.0);
        }

        @Test
        @DisplayName("여행정보 동적조회 테스트 - 리뷰 많은 순")
        public void 여행정보_동적조회3() {
            // given
            Pageable pageable = PageRequest.of(0, 10);

            // when
            List<TripEntity> trips =
                    tripRepository.searchTripsAndSort(null, null, TripSort.REVIEWS, pageable);

            // then
            assertThat(trips.get(0).getReviewList().size())
                    .isGreaterThanOrEqualTo(trips.get(1).getReviewList().size());
            assertThat(trips.get(1).getReviewList().size())
                    .isGreaterThanOrEqualTo(trips.get(2).getReviewList().size());
        }
    }

    private TripEntity createTripEntity(String name, String description) {
        TripEntity trip =
                TripEntity.builder()
                        .city(TripTest.TEST_CITY)
                        .category(Category.ATTRACTION)
                        .name(name)
                        .description(description)
                        .build();
        return tripRepository.save(trip);
    }

    @Transactional
    protected void addReviewsToTrip(TripEntity trip, int score, int count) {
        for (int i = 0; i < count; i++) {
            ReviewEntity review =
                    ReviewEntity.builder()
                            .user(TripTest.TEST_USER)
                            .content("Test Review " + (i + 1))
                            .score(score)
                            .trip(trip)
                            .build();
            reviewRepository.save(review);
            trip.getReviewList().add(review);
        }
        trip.calculateAverageRating();
        tripRepository.save(trip);
    }
}
