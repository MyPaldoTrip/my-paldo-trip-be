package com.b6.mypaldotrip.domain.tripFile.controller;

import com.b6.mypaldotrip.domain.tripFile.controller.dto.TripFileGetRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileDeleteRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileListRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileUploadRes;
import com.b6.mypaldotrip.domain.tripFile.service.TripFileService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/${mpt.version}/trips/{tripId}/files")
@RequiredArgsConstructor
public class TripFileController {

    private final TripFileService tripFileService;
    private final VersionConfig versionConfig;

    @PostMapping
    public ResponseEntity<RestResponse<TripFileUploadRes>> uploadTripFile(
            @PathVariable Long tripId,
            @RequestPart MultipartFile multipartFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws IOException {
        TripFileUploadRes res = tripFileService.uploadTrip(tripId, multipartFile);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping
    public ResponseEntity<RestResponse<List<TripFileListRes>>> getTripFileList(
            @PathVariable Long tripId) {
        List<TripFileListRes> res = tripFileService.getTripFileList(tripId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/{tripFileId}")
    public ResponseEntity<RestResponse<TripFileGetRes>> getTripFile(
            @PathVariable Long tripId, @PathVariable Long tripFileId) {
        TripFileGetRes res = tripFileService.getTripFile(tripId, tripFileId);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @DeleteMapping("/{tripFileId}")
    public ResponseEntity<RestResponse<TripFileDeleteRes>> deleteTripFile(
            @PathVariable Long tripId,
            @PathVariable Long tripFileId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TripFileDeleteRes res = tripFileService.deleteTripFile(tripId, tripFileId, userDetails);
        return RestResponse.success(res, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }
}
