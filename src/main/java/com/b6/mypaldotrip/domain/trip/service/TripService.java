package com.b6.mypaldotrip.domain.trip.service;

import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripListReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.*;
import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.entity.TripSort;
import com.b6.mypaldotrip.domain.trip.store.repository.TripRepository;
import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import com.b6.mypaldotrip.domain.tripFile.store.repository.TripFileRepository;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final CityService cityService;
    private final S3Provider s3Provider;
    private final TripFileRepository tripFileRepository;

    @Transactional
    public TripCreateRes createTrip(
            String reqJson, MultipartFile multipartFile, UserDetailsImpl userDetails)
            throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        TripCreateReq req = objectMapper.readValue(reqJson, TripCreateReq.class);

        String name = req.name();
        checkAuthorization(userDetails);
        if (tripRepository.findByName(name).isPresent()) {
            throw new GlobalException(TripErrorCode.ALREADY_EXIST_TRIP);
        }
        TripEntity trip =
                TripEntity.builder()
                        .city(cityService.findCity(req.cityId()))
                        .category(req.category())
                        .name(req.name())
                        .description(req.description())
                        .build();

        if (multipartFile != null) {
            String fileUrl = s3Provider.saveFile(multipartFile, "trip");
            TripFileEntity tripFile = TripFileEntity.builder().trip(trip).fileUrl(fileUrl).build();
            tripFileRepository.save(tripFile);
        }

        tripRepository.save(trip);

        return TripCreateRes.builder()
                .city(trip.getCity().getCityName())
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .tripFileList(trip.getTripFileList())
                .build();
    }

    public List<TripListRes> getTripList(TripListReq req) {
        Pageable pageable = PageRequest.of(req.page(), 20);
        TripSort sort = (req.tripSort() != null) ? req.tripSort() : TripSort.CREATED;
        return tripRepository
                .searchTripsAndSort(req.cityName(), req.category(), sort, pageable)
                .stream()
                .map(
                        trip ->
                                TripListRes.builder()
                                        .tripId(trip.getTripId())
                                        .city(trip.getCity().getCityName())
                                        .category(trip.getCategory())
                                        .name(trip.getName())
                                        .averageRating(trip.getAverageRating())
                                        .reviews(trip.getReviewList().size())
                                        .build())
                .toList();
    }

    @Transactional
    public TripGetRes getTrip(Long tripId) {
        TripEntity trip = findTrip(tripId);

        List<String> fileUrlList = new ArrayList<>();
        for (TripFileEntity tripFile : trip.getTripFileList()) {
            fileUrlList.add(tripFile.getFileUrl());
        }

        return TripGetRes.builder()
                .tripId(trip.getTripId())
                .city(trip.getCity().getCityName())
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .urlList(fileUrlList)
                .build();
    }

    @Transactional
    public TripUpdateRes updateTrip(Long tripId, TripUpdateReq req, UserDetailsImpl userDetails) {
        checkAuthorization(userDetails);
        TripEntity trip = findTrip(tripId);
        trip.updateTrip(
                cityService.findCity(req.cityId()), req.category(), req.name(), req.description());
        return TripUpdateRes.builder()
                .city(trip.getCity().getCityName())
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .build();
    }

    public TripDeleteRes deleteTrip(Long tripId, UserDetailsImpl userDetails) {
        checkAuthorization(userDetails);
        TripEntity trip = findTrip(tripId);
        tripRepository.delete(trip);
        return TripDeleteRes.builder().message("여행 정보가 삭제되었습니다.").build();
    }

    // 권한 체크 메서드
    private static void checkAuthorization(UserDetailsImpl userDetails) {
        if (userDetails.getUserEntity().getUserRole() == UserRole.ROLE_USER) {
            throw new GlobalException(TripErrorCode.UNAUTHORIZED_ROLE_ERROR);
        }
    }

    // 여행정보 조회 메서드
    public TripEntity findTrip(Long tripId) {
        return tripRepository
                .findById(tripId)
                .orElseThrow(() -> new GlobalException(TripErrorCode.NON_EXIST_TRIP));
    }
}
