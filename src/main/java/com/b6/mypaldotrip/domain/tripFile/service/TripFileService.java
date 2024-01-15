package com.b6.mypaldotrip.domain.tripFile.service;

import com.b6.mypaldotrip.domain.trip.service.TripService;
import com.b6.mypaldotrip.domain.trip.store.entity.TripEntity;
import com.b6.mypaldotrip.domain.tripFile.controller.dto.response.TripFileUploadRes;
import com.b6.mypaldotrip.domain.tripFile.store.entity.TripFileEntity;
import com.b6.mypaldotrip.domain.tripFile.store.repository.TripFileRepository;
import com.b6.mypaldotrip.global.common.S3Provider;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public void saveTripFile(TripFileEntity tripFile) {
        tripFileRepository.save(tripFile);
    }
}
