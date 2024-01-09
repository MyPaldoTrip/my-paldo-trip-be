package com.b6.mypaldotrip.domain.city.service;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
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

        //유저의 권한이 관리자인지 확인(securityconfig에서 관리자만 할수 있게 하는지)

        //같은 시 명이 중복되는지 확인
        findCity(req.cityName());

        CityEntity cityEntity = CityEntity.builder()
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
        //수정하려는 시가 존재하는지 확인
        CityEntity cityEntity = findCity(cityId);
        //같은 시 명이 중복되는지 확인
        //findCity(req.cityName());

        cityEntity.update(req.provinceName(), req.cityName(), req.cityInfo());

        return CityUpdateRes
            .builder()
            .provinceName(cityEntity.getProvinceName())
            .cityName(cityEntity.getCityName())
            .cityInfo(cityEntity.getCityInfo())
            .build();
    }

    public CityDeleteRes deleteCity(Long cityId) {
        CityEntity cityEntity = findCity(cityId);
        cityRepository.delete(cityEntity);
        CityDeleteRes res = CityDeleteRes.builder()
            .msg(cityEntity.getProvinceName() + cityEntity.getCityName() + "를 삭제했습니다")
            .build();

        return res;
    }

    public List<CityEntity> getProvinceList() {
        //List<CityEntity> provinces = cityRepository.findDistinctProvinceNames();

        return null;
    }

    public List<CityListRes> getCityList(String cityName) {
        return null;
    }

    private CityEntity findCity(Long cityId) {//존재하는지 확인을 위해 생성
        return cityRepository.findById(cityId).orElseThrow(
            () -> new GlobalException(CityErrorCode.CITY_NOT_FOUND)
        );
    }

    private void findCity(String cityName) {//중복체크를 위해 생성
        if (cityRepository.findByCityName(cityName).isPresent()) {
            throw new GlobalException(CityErrorCode.ALREADY_CITY_EXIST);
        }
    }


}
