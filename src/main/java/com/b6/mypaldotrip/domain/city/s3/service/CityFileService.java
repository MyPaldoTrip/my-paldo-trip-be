package com.b6.mypaldotrip.domain.city.s3.service;

import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.s3.dto.CItyFileListRes;
import com.b6.mypaldotrip.domain.city.s3.dto.CityFileRes;
import com.b6.mypaldotrip.domain.city.s3.entity.CityFileEntity;
import com.b6.mypaldotrip.domain.city.s3.repository.CityFileRepository;
import com.b6.mypaldotrip.domain.city.service.CityService;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CityFileService {

    private final S3Provider s3Provider;
    private final CityFileRepository cityFileRepository;
    private final CityService cityService;

    public CityFileRes saveFile(Long cityId, MultipartFile multipartFile) throws IOException {
        CityEntity city = cityService.findCity(cityId);
        String fileURL = s3Provider.saveFile(multipartFile, "city");
        cityFileRepository.save(CityFileEntity.builder().fileUrl(fileURL).cityEntity(city).build());
        return CityFileRes.builder().msg("파일이 성공적으로 업로드 되었습니다.").build();
    }

    @Transactional
    public CityFileRes updateFile(Long fileId, MultipartFile multipartFile) throws IOException {
        CityFileEntity cityFileEntity = getCityFileEntity(fileId);
        s3Provider.updateFile(cityFileEntity, multipartFile);
        cityFileRepository.save(cityFileEntity);

        return CityFileRes.builder().msg("파일이 성공적으로 수정되었습니다.").build();
    }

    public CityFileRes deleteFile(Long fileId) {
        CityFileEntity cityFileEntity = getCityFileEntity(fileId);
        cityFileRepository.delete(cityFileEntity);
        s3Provider.deleteFile(cityFileEntity);
        return CityFileRes.builder().msg("파일이 성공적으로 삭제 되었습니다.").build();
    }

    public List<CItyFileListRes> getFileList(Long cityId) {
        CityEntity city = cityService.findCity(cityId);

        List<CItyFileListRes> res =
                cityFileRepository.findAllByCityEntity(city).stream()
                        .map(
                                s3List ->
                                        CItyFileListRes.builder()
                                                .Id(s3List.Id())
                                                .FileURL(s3List.FileURL())
                                                .build())
                        .toList();
        return res;
    }

    public CityFileEntity getCityFileEntity(Long fileId) {
        CityFileEntity cityFileEntity =
                cityFileRepository
                        .findById(fileId)
                        .orElseThrow(() -> new GlobalException(CityErrorCode.FILE_NOT_FOUND));
        return cityFileEntity;
    }
}
