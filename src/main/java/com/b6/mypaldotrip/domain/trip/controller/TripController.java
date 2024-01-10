package com.b6.mypaldotrip.domain.trip.controller;

import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripCreateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripListReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.*;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/${mpt.version}/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<TripCreateRes>> createTrip(
            @Valid @RequestBody TripCreateReq req) {
        TripCreateRes res = tripService.createTrip(req);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<TripListRes>>> getTripList(
            @RequestBody TripListReq req) {
        List<TripListRes> res = tripService.getTripList(req);
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
    public ResponseEntity<RestResponse<TripUpdateRes>> updateTrip(
            @PathVariable Long tripId, @RequestBody TripUpdateReq req) {
        TripUpdateRes res = tripService.updateTrip(tripId, req);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<RestResponse<TripDeleteRes>> deleteTrip(@PathVariable Long tripId) {
        TripDeleteRes res = tripService.deleteTrip(tripId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
