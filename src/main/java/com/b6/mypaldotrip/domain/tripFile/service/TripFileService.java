package com.b6.mypaldotrip.domain.tripFile.service;

import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileListRes;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileUploadRes;
import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import com.b6.mypaldotrip.domain.tripFile.store.repository.TripFileRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
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
        TripEntity trip = tripService.findTrip(tripId);
        TripFileEntity tripFile = TripFileEntity.builder().trip(trip).fileUrl(fileUrl).build();
        saveTripFile(tripFile);
        return TripFileUploadRes.builder()
                .tripFileId(tripFile.getTripFileId())
                .fileUrl(tripFile.getFileUrl())
                .build();
    }

    @Transactional
    public List<TripFileListRes> getTripFileList(Long tripId) {
        TripEntity trip = tripService.findTrip(tripId);
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

    public void saveTripFile(TripFileEntity tripFile) {
        tripFileRepository.save(tripFile);
    }
}
