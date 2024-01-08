package com.b6.mypaldotrip.domain.trip.controller;

import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripCreateRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripGetRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripListRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripUpdateRes;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<TripCreateRes>> createTrip(@Valid @RequestBody TripCreateReq req) {
        TripCreateRes res = tripService.createTrip(req);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<TripListRes>>> getTripList() {
        List<TripListRes> res = tripService.getTripList();
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<RestResponse<TripGetRes>> getTrip(@PathVariable Long tripId) {
        TripGetRes res = tripService.getTrip(tripId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PatchMapping("/{tripId}")
    public ResponseEntity<RestResponse<TripUpdateRes>> updateTrip(@PathVariable Long tripId,
                                                                  @RequestBody TripUpdateReq req) {
        TripUpdateRes res = tripService.updateTrip(tripId, req);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
