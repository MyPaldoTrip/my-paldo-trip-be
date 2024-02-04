package com.b6.mypaldotrip.domain.trip.controller;

import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripListReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.request.TripUpdateReq;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripCreateRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripDeleteRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripGetRes;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripListWrapper;
import com.b6.mypaldotrip.domain.trip.controller.dto.response.TripUpdateRes;
import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/${mpt.version}/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<TripCreateRes>> createTrip(
            @Valid @RequestPart String req,
            @RequestPart MultipartFile multipartFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws IOException {
        TripCreateRes res = tripService.createTrip(req, multipartFile, userDetails);
        return RestResponse.success(res, GlobalResultCode.CREATED, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PostMapping("/lists")
    public ResponseEntity<RestResponse<TripListWrapper>> getTripList(
            @RequestBody TripListReq req) {
        TripListWrapper res = tripService.getTripList(req);
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
            @PathVariable Long tripId,
            @RequestBody TripUpdateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TripUpdateRes res = tripService.updateTrip(tripId, req, userDetails);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<RestResponse<TripDeleteRes>> deleteTrip(
            @PathVariable Long tripId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TripDeleteRes res = tripService.deleteTrip(tripId, userDetails);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
