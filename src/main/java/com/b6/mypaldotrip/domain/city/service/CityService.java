package com.b6.mypaldotrip.domain.city.service;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.ProvinceListReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.ProvinceListRes;
import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.s3.entity.CityFileEntity;
import com.b6.mypaldotrip.domain.city.s3.repository.CityFileRepository;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.entity.CitySort;
import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.domain.trip.exception.TripErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.common.S3Provider;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityFileRepository cityFileRepository;
    private final S3Provider s3Provider;

    public CityCreateRes createCity(String req, UserDetailsImpl userDetails, MultipartFile multipartFile)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CityCreateReq cityCreateReq = objectMapper.readValue(req, CityCreateReq.class);
        // 유저의 권한이 관리자인지 확인(securityconfig에서 관리자만 할수 있게 하는지)
        checkAuthorization(userDetails);
        // 같은 시 명이 중복되는지 확인
        cityDuplicationCheck(cityCreateReq.cityName());
        // MultipartFile이 null이 아니고 이미지가 아닌 경우 예외 처리
        if (multipartFile != null && !isImage(multipartFile)) {
            throw new GlobalException(CityErrorCode.WRONG_FILE_EXTENSION);
        }
        CityEntity cityEntity =
                CityEntity.builder()
                        .provinceName(cityCreateReq.provinceName())
                        .cityName(cityCreateReq.cityName())
                        .cityInfo(cityCreateReq.cityInfo())
                        .build();

        // MultipartFile이 null이 아니면 이미지 업로드 및 저장
        if (multipartFile != null) {
            String fileURL = s3Provider.saveFile(multipartFile, "city");
            cityFileRepository.save(
                CityFileEntity.builder().fileUrl(fileURL).cityEntity(cityEntity).build());
        }
        cityRepository.save(cityEntity);

        return CityCreateRes.builder()
                .provinceName(cityEntity.getProvinceName())
                .cityName(cityEntity.getCityName())
                .cityInfo(cityEntity.getCityInfo())
                .build();
    }

    @Transactional
    public CityUpdateRes updateCity(Long cityId, CityUpdateReq req,UserDetailsImpl userDetails) {
        checkAuthorization(userDetails);
        // 수정하려는 시가 존재하는지 확인
        CityEntity cityEntity = findCity(cityId);
        cityEntity.update(req.provinceName(), req.cityName(), req.cityInfo());

        return CityUpdateRes.builder()
                .provinceName(cityEntity.getProvinceName())
                .cityName(cityEntity.getCityName())
                .cityInfo(cityEntity.getCityInfo())
                .build();
    }

    public CityDeleteRes deleteCity(Long cityId,UserDetailsImpl userDetails) {
        checkAuthorization(userDetails);
        CityEntity cityEntity = findCity(cityId);
        cityRepository.delete(cityEntity);
        CityDeleteRes res =
                CityDeleteRes.builder()
                        .msg(
                                cityEntity.getProvinceName()
                                        + " "
                                        + cityEntity.getCityName()
                                        + " 삭제 완료")
                        .build();

        return res;
    }

    public List<ProvinceListRes> getProvinceList() {
        List<String> provinces = cityRepository.findDistinctByProvinceName();
        if (provinces.isEmpty()) {
            throw new GlobalException(CityErrorCode.PROVINCE_NOT_FOUND);
        }
        List<ProvinceListRes> res =
                provinces.stream()
                        .map(province -> ProvinceListRes.builder().provinceName(province).build())
                        .toList();
        return res;
    }

    public List<CityListRes> getCityList(String provinceName) {
        List<CityListRes> res = cityRepository.findByProvinceName(provinceName);
        if (res.isEmpty()) {
            throw new GlobalException(CityErrorCode.CITY_NOT_FOUND);
        }
        return res;
    }

    public List<ProvinceListRes> getProvinceListInfoSort(ProvinceListReq req) { // 여행정보 많은 순 정렬
        CitySort sort = (req.citySort() != null) ? req.citySort() : CitySort.INITIAL;

        List<ProvinceListRes> res =
                cityRepository.getProvinceSort(sort).stream()
                        .map(
                                city ->
                                        ProvinceListRes.builder()
                                                .provinceName(city.getProvinceName())
                                                .build())
                        .toList();
        return res;
    }

    public CityEntity findCity(Long cityId) { // 존재하는지 확인을 위해 생성
        return cityRepository
                .findById(cityId)
                .orElseThrow(() -> new GlobalException(CityErrorCode.CITY_NOT_FOUND));
    }

    public CityEntity findByCityName(String cityName) {
        return cityRepository
                .findByCityName(cityName)
                .orElseThrow(() -> new GlobalException(CityErrorCode.CITY_NOT_FOUND));
    }

    public void cityDuplicationCheck(String cityName) { // 중복체크를 위해 생성
        if (cityRepository.findByCityName(cityName).isPresent()) {
            throw new GlobalException(CityErrorCode.ALREADY_CITY_EXIST);
        }
    }
    private static void checkAuthorization(UserDetailsImpl userDetails) {
        if (userDetails.getUserEntity().getUserRole() == UserRole.ROLE_USER) {
            throw new GlobalException(TripErrorCode.UNAUTHORIZED_ROLE_ERROR);
        }
    }

    public boolean isImage(MultipartFile file) {

        String contentType = file.getContentType();

        if (contentType != null && contentType.startsWith("image")) {
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                String lowerCaseFileName = fileName.toLowerCase();
                if (lowerCaseFileName.endsWith(".gif")) {
                    return false; // 잘못된 확장자인 경우 false 반환
                }
                return true;
            }
        }
        return false;
}
}
