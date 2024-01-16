package com.b6.mypaldotrip.domain.city.s3.service;

import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.s3.dto.S3Res;
import com.b6.mypaldotrip.domain.city.s3.entity.S3Entity;
import com.b6.mypaldotrip.domain.city.s3.repository.S3Repository;
import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Provider s3Provider;
    private final S3Repository s3Repository;
    private final CityService cityService;

    public S3Res saveFile(Long cityId, MultipartFile multipartFile) throws IOException {
        CityEntity city = cityService.findCity(cityId);
        String fileURL = s3Provider.saveFile(multipartFile, "city");
        s3Repository.save(S3Entity.builder().fileUrl(fileURL).cityEntity(city).build());
        return S3Res.builder().msg("파일이 성공적으로 업로드 되었습니다.").build();
    }

    @Transactional
    public S3Res updateFile(Long fileId, MultipartFile multipartFile) throws IOException {
        S3Entity s3Entity =
                s3Repository
                        .findById(fileId)
                        .orElseThrow(() -> new GlobalException(CityErrorCode.FILE_NOT_FOUND));
        s3Provider.updateFile(s3Entity, multipartFile);
        s3Repository.save(s3Entity);

        return S3Res.builder().msg("파일이 성공적으로 수정되었습니다.").build();
    }

    public S3Res deleteFile(Long fileId) {
        S3Entity s3Entity =
                s3Repository
                        .findById(fileId)
                        .orElseThrow(() -> new GlobalException(CityErrorCode.FILE_NOT_FOUND));
        s3Repository.delete(s3Entity);
        s3Provider.deleteFile(s3Entity);
        return S3Res.builder().msg("파일이 성공적으로 삭제 되었습니다.").build();
    }
}
