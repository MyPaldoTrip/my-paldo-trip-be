package com.b6.mypaldotrip.domain.tripFile.service;

import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.TripFileGetRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileDeleteRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileListRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileUploadRes;
import com.b6.mypaldotrip.domain.tripFile.exception.TripFileErrorCode;
import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import com.b6.mypaldotrip.domain.tripFile.store.repository.TripFileRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TripFileService {

    private final TripFileRepository tripFileRepository;
    private final TripService tripService;
    private final S3Provider s3Provider;

    public TripFileUploadRes uploadTrip(Long tripId, MultipartFile multipartFile)
            throws IOException {
        String fileUrl = s3Provider.saveFile(multipartFile, "trip");
        TripEntity trip = checkTripId(tripId);
        TripFileEntity tripFile = TripFileEntity.builder().trip(trip).fileUrl(fileUrl).build();
        tripFileRepository.save(tripFile);
        return TripFileUploadRes.builder()
                .tripFileId(tripFile.getTripFileId())
                .fileUrl(tripFile.getFileUrl())
                .build();
    }

    @Transactional
    public List<TripFileListRes> getTripFileList(Long tripId) {
        TripEntity trip = checkTripId(tripId);
        List<TripFileEntity> tripFileEntityList = trip.getTripFileList();
        List<TripFileListRes> tripFileListRes = new ArrayList<>();
        for (TripFileEntity tripFile : tripFileEntityList) {
            TripFileListRes res =
                    TripFileListRes.builder()
                            .tripFileId(tripFile.getTripFileId())
                            .fileUrl(tripFile.getFileUrl())
                            .build();
            tripFileListRes.add(res);
        }
        return tripFileListRes;
    }

    public TripFileGetRes getTripFile(Long tripId, Long tripFileId) {
        TripFileEntity tripFile = checkTripAndFile(tripId, tripFileId);
        return TripFileGetRes.builder()
                .tripFileId(tripFile.getTripFileId())
                .fileUrl(tripFile.getFileUrl())
                .build();
    }

    public TripFileDeleteRes deleteTripFile(
            Long tripId, Long tripFileId, UserDetailsImpl userDetails) {
        TripFileEntity tripFile = checkTripAndFile(tripId, tripFileId);
        tripFileRepository.delete(tripFile);
        return TripFileDeleteRes.builder().message("파일이 삭제 되었습니다.").build();
    }

    // 여행정보와 파일 검증 메서드
    private TripFileEntity checkTripAndFile(Long tripId, Long tripFileId) {
        TripFileEntity tripFile =
                tripFileRepository.findByTrip_TripIdAndTripFileId(tripId, tripFileId);
        if (tripFile == null) {
            throw new GlobalException(TripFileErrorCode.NON_EXIST_FILE);
        }
        return tripFile;
    }

    // 여행정보 검증 메서드
    private TripEntity checkTripId(Long tripId) {
        TripEntity trip = tripService.findTrip(tripId);
        return trip;
    }
}
