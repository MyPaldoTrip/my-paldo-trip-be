package com.b6.mypaldotrip.domain.city.service;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.ProvinceListRes;
import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.city.store.entity.CitySort;
import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityCreateRes createCity(CityCreateReq req) {

        // 유저의 권한이 관리자인지 확인(securityconfig에서 관리자만 할수 있게 하는지)

        // 같은 시 명이 중복되는지 확인
        cityDuplicationCheck(req.cityName());

        CityEntity cityEntity =
                CityEntity.builder()
                        .provinceName(req.provinceName())
                        .cityName(req.cityName())
                        .cityInfo(req.cityInfo())
                        .build();

        cityRepository.save(cityEntity);

        return CityCreateRes.builder()
                .provinceName(cityEntity.getProvinceName())
                .cityName(cityEntity.getCityName())
                .cityInfo(cityEntity.getCityInfo())
                .build();
    }

    @Transactional
    public CityUpdateRes updateCity(Long cityId, CityUpdateReq req) {
        // 수정하려는 시가 존재하는지 확인
        CityEntity cityEntity = findCity(cityId);

        cityEntity.update(req.provinceName(), req.cityName(), req.cityInfo());

        return CityUpdateRes.builder()
                .provinceName(cityEntity.getProvinceName())
                .cityName(cityEntity.getCityName())
                .cityInfo(cityEntity.getCityInfo())
                .build();
    }

    public CityDeleteRes deleteCity(Long cityId) {
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

    public List<ProvinceListRes> getProvinceListInfoSort() { // 여행정보 많은 순 정렬
        CitySort sort = CitySort.COUNT;

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
}
