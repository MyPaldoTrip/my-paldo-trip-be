package com.b6.mypaldotrip.domain.trip.service;

import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.*;
import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.trip.store.repository.TripRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<TripListRes> getTripList() {
        return findAllTrips().stream()
                .map(trip -> TripListRes.builder()
                        .category(trip.getCategory())
                        .name(trip.getName())
                        .build()
                ).toList();
    }

    public TripGetRes getTrip(Long tripId) {
        TripEntity trip = findTrip(tripId);
        return TripGetRes.builder()
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .build();
    }

    @Transactional
    public TripUpdateRes updateTrip(Long tripId, TripUpdateReq req) {
        // TODO: 2024-01-08 운영자 검증 로직 추가 필요
        TripEntity trip = findTrip(tripId);
        trip.updateTrip(req.category(), req.name(), req.description());
        return TripUpdateRes.builder()
                .category(trip.getCategory())
                .name(trip.getName())
                .description(trip.getDescription())
                .build();
    }

    public TripDeleteRes deleteTrip(Long tripId) {
        // TODO: 2024-01-08 운영자 검증 로직 추가 필요
        TripEntity trip = findTrip(tripId);
        tripRepository.delete(trip);
        return TripDeleteRes.builder().message("여행 정보가 삭제되었습니다.").build();
    }

    // 여행정보 전체 조회 메서드
    private List<TripEntity> findAllTrips() {
        return tripRepository.findAll();
    }

    // 여행정보 조회 메서드
    private TripEntity findTrip(Long tripId) {
        return tripRepository.findById(tripId).orElseThrow(() -> new GlobalException(TripErrorCode.NON_EXIST_TRIP));
    }
}
