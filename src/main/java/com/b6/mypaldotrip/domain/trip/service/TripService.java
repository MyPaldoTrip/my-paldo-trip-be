package com.b6.mypaldotrip.domain.trip.service;

import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripCreateRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripRes;
import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.repository.TripRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public TripCreateRes createTrip(TripCreateReq req) {
        String name = req.name();

        /** TODO: 2024-01-08 운영자 검증 로직 추가 필요
         *  TODO: 2024-01-08 city 정보 검증 로직 추가 필요
         */
        if (req.category() == null) {
            throw new GlobalException(TripErrorCode.WRONG_CATEGORY_ERROR);
        } else if (req.name().isEmpty()) {
            throw new GlobalException(TripErrorCode.NO_NAME_ERROR);
        } else if (req.description().isEmpty()) {
            throw new GlobalException(TripErrorCode.NO_DESCRIPTION_ERROR);
        } else if (tripRepository.findByName(name).isPresent()) {
            throw new GlobalException(TripErrorCode.ALREADY_EXIST_TRIP);
        }
        TripEntity trip = TripEntity.builder()
                .category(req.category())
                .name(req.name())
                .description(req.description())
                .build();
        tripRepository.save(trip);
        return TripCreateRes.builder()
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription()).build();
    }

    public List<TripRes> getAllTrips() {
        List<TripEntity> tripList = findAllTrips();
        List<TripRes> tripResList = new ArrayList<>();

        for (TripEntity trip : tripList) {
            tripResList.add(new TripRes(trip.getCategory(), trip.getName(), trip.getDescription()));
        }
        return tripResList;
    }

    // 모든 여행정보 조회 메서드
    private List<TripEntity> findAllTrips() {
        return tripRepository.findAll();
    }
}
