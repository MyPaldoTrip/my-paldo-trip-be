package com.b6.mypaldotrip.domain.trip.service;

import static com.b6.mypaldotrip.domain.user.store.entity.UserRole.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.trip.TripTest;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripListReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.*;
import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.trip.store.entity.Category;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.repository.TripRepository;
import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import com.b6.mypaldotrip.domain.tripFile.store.repository.TripFileRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest implements TripTest {
    @InjectMocks private TripService tripService;
    @Mock private TripRepository tripRepository;
    @Mock private CityService cityService;
    @Mock private S3Provider s3Provider;
    @Mock private TripFileRepository tripFileRepository;

    @Nested
    @DisplayName("여행정보 생성 테스트")
    class 여행정보_생성 {

        @Test
        @DisplayName("여행정보 생성 테스트 성공")
        void 여행정보_생성1() throws IOException {
            // given
            ObjectMapper objectMapper = new ObjectMapper();
            TripCreateReq req =
                    TripCreateReq.builder()
                            .cityName(TEST_CITY_NAME)
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .build();
            MockMultipartFile file =
                    new MockMultipartFile(
                            "multipartfile", "test.jpg", "image/jpeg", "image bytes".getBytes());
            TEST_USER.acceptPermission();

            // when
            when(cityService.findByCityName(TEST_CITY_NAME)).thenReturn(TEST_CITY);
            when(tripRepository.findByName(TEST_TRIP_NAME)).thenReturn(Optional.empty());
            when(s3Provider.saveFile(any(), anyString())).thenReturn(TEST_FILE_URL);
            when(tripRepository.save(any(TripEntity.class))).thenReturn(TEST_TRIP);
            TripCreateRes res =
                    tripService.createTrip(
                            objectMapper.writeValueAsString(req),
                            file,
                            new UserDetailsImpl(TEST_USER));

            // then
            assertThat(res.name()).isEqualTo(TEST_TRIP_NAME);
            assertThat(res.city()).isEqualTo(TEST_CITY_NAME);
            assertThat(res.category()).isEqualTo(req.category());
            assertThat(res.description()).isEqualTo(TEST_DESCRIPTION);
            assertThat(res.tripFileList()).isEqualTo(TEST_TRIP.getTripFileList());

            verify(tripFileRepository, times(1)).save(any(TripFileEntity.class));
        }

        @Test
        @DisplayName("여행정보 생성 테스트 실패 - 도시가 존재하지 않는 경우")
        void 여행정보_생성2() throws IOException {
            // given
            ObjectMapper objectMapper = new ObjectMapper();
            TripCreateReq req =
                    TripCreateReq.builder()
                            .cityName("not exist city")
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .build();
            MockMultipartFile file =
                    new MockMultipartFile(
                            "multipartfile", "test.jpg", "image/jpeg", "image bytes".getBytes());
            TEST_USER.acceptPermission();

            // when
            when(cityService.findByCityName("not exist city")).thenReturn(null);

            // then
            Throwable throwable =
                    catchThrowable(
                            () ->
                                    tripService.createTrip(
                                            objectMapper.writeValueAsString(req),
                                            file,
                                            new UserDetailsImpl(TEST_USER)));
            assertThat(throwable).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("여행정보 생성 테스트 실패 - 동일한 이름의 여행이 존재하는 경우")
        void 여행정보_생성3() throws IOException {
            // given
            ObjectMapper objectMapper = new ObjectMapper();
            TripCreateReq req =
                    TripCreateReq.builder()
                            .cityName(TEST_CITY_NAME)
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .build();
            MockMultipartFile file =
                    new MockMultipartFile(
                            "multipartfile", "test.jpg", "image/jpeg", "image bytes".getBytes());
            TEST_USER.acceptPermission();

            // when
            when(tripRepository.findByName(TEST_TRIP_NAME)).thenReturn(Optional.of(TEST_TRIP));

            // then
            Throwable thrown =
                    catchThrowable(
                            () ->
                                    tripService.createTrip(
                                            objectMapper.writeValueAsString(req),
                                            file,
                                            new UserDetailsImpl(TEST_USER)));
            assertThat(thrown).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("여행정보 생성 테스트 실패 - 첨부파일이 존재하지 않는 경우")
        void 여행정보_생성4() throws IOException {
            // given
            ObjectMapper objectMapper = new ObjectMapper();
            TripCreateReq req =
                    TripCreateReq.builder()
                            .cityName(TEST_CITY_NAME)
                            .category(Category.ATTRACTION)
                            .name(TEST_TRIP_NAME)
                            .description(TEST_DESCRIPTION)
                            .build();
            MockMultipartFile file = null;
            TEST_USER.acceptPermission();

            // when
            when(cityService.findByCityName(TEST_CITY_NAME)).thenReturn(TEST_CITY);
            when(tripRepository.findByName(TEST_TRIP_NAME)).thenReturn(Optional.empty());

            // then
            Throwable thrown =
                    catchThrowable(
                            () ->
                                    tripService.createTrip(
                                            objectMapper.writeValueAsString(req),
                                            file,
                                            new UserDetailsImpl(TEST_USER)));
            assertThat(thrown).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    @DisplayName("여행정보 목록 조회 테스트 성공")
    void 여행정보_목록조회() {
        // given
        TripListReq req = TripListReq.builder().build();
        List<TripEntity> tripEntityList = List.of(TEST_TRIP);
        given(tripRepository.searchTripsAndSort(any(), any(), any(), any()))
                .willReturn(tripEntityList);

        // when
        List<TripListRes> res = tripService.getTripList(req);

        // then
        assertThat(res.get(0).tripId()).isEqualTo(TEST_TRIP.getTripId());
    }

    @Test
    @DisplayName("여행정보 단건 조회 테스트 성공")
    void 여행정보_단건조회() {
        // given
        given(tripRepository.findById(TEST_TRIPID)).willReturn(Optional.of(TEST_TRIP));

        // when
        TripGetRes res = tripService.getTrip(TEST_TRIPID);

        // then
        assertThat(res.tripId()).isEqualTo(TEST_TRIP.getTripId());
        assertThat(res.city()).isEqualTo(TEST_TRIP.getCity().getCityName());
        assertThat(res.category()).isEqualTo(TEST_TRIP.getCategory());
        assertThat(res.name()).isEqualTo(TEST_TRIP.getName());
        assertThat(res.description()).isEqualTo(TEST_TRIP.getDescription());
    }

    @Nested
    @DisplayName("여행정보 수정 테스트")
    class 여행정보_수정 {

        @Test
        @DisplayName("여행정보 수정 테스트 성공")
        void 여행정보_수정1() {
            // given
            TripUpdateReq req = TripUpdateReq.builder().build();
            TEST_USER.acceptPermission();
            given(tripRepository.findById(TEST_TRIPID)).willReturn(Optional.of(TEST_TRIP));
            given(cityService.findByCityName(req.cityName())).willReturn(TEST_CITY);

            // when
            TripUpdateRes res =
                    tripService.updateTrip(TEST_TRIPID, req, new UserDetailsImpl(TEST_USER));

            // then
            assertThat(res.city()).isEqualTo(TEST_CITY.getCityName());
            assertThat(res.category()).isEqualTo(TEST_TRIP.getCategory());
            assertThat(res.name()).isEqualTo(TEST_TRIP.getName());
            assertThat(res.description()).isEqualTo(TEST_TRIP.getDescription());
        }

        @Test
        @DisplayName("여행정보 수정 테스트 실패 - 여행정보가 존재하지 않을 때")
        void 여행정보_수정2() {
            // given
            TripUpdateReq req = TripUpdateReq.builder().build();
            TEST_USER.acceptPermission();
            given(tripRepository.findById(TEST_TRIPID)).willReturn(Optional.empty());

            // when

            // then
            Throwable thrown =
                    catchThrowable(
                            () -> {
                                tripService.updateTrip(
                                        TEST_TRIPID, req, new UserDetailsImpl(TEST_USER));
                            });
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class)
                    .extracting(th -> ((GlobalException) th).getResultCode().getMessage())
                    .isEqualTo(TripErrorCode.NON_EXIST_TRIP.getMessage());
        }
    }

    @Nested
    @DisplayName("여행정보 삭제 테스트")
    class 여행정보_삭제 {

        @Test
        @DisplayName("여행정보 삭제 테스트 성공")
        void 여행정보_삭제1() {
            // given
            TEST_USER.acceptPermission();
            given(tripRepository.findById(TEST_TRIPID)).willReturn(Optional.of(TEST_TRIP));

            // when
            TripDeleteRes res = tripService.deleteTrip(TEST_TRIPID, new UserDetailsImpl(TEST_USER));

            // then
            assertThat(res.message()).isEqualTo("여행 정보가 삭제되었습니다.");
            verify(tripRepository, times(1)).delete(TEST_TRIP);
        }

        @Test
        @DisplayName("여행정보 삭제 테스트 실패 - 권한이 없을 때")
        void 여행정보_삭제2() {
            // given
            ReflectionTestUtils.setField(TEST_USER, "userRole", ROLE_USER);

            // when

            // then
            Throwable thrown =
                    catchThrowable(
                            () -> {
                                tripService.deleteTrip(TEST_TRIPID, new UserDetailsImpl(TEST_USER));
                            });
            assertThat(thrown)
                    .isInstanceOf(GlobalException.class)
                    .extracting(th -> ((GlobalException) th).getResultCode().getMessage())
                    .isEqualTo(TripErrorCode.UNAUTHORIZED_ROLE_ERROR.getMessage());
        }
    }
}
