package com.b6.mypaldotrip.domain.city.service;

import com.b6.mypaldotrip.domain.city.controller.dto.request.CityCreateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.request.CityUpdateReq;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityCreateRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityDeleteRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityListRes;
import com.b6.mypaldotrip.domain.city.controller.dto.response.CityUpdateRes;
import com.b6.mypaldotrip.domain.city.exception.CityErrorCode;
import com.b6.mypaldotrip.domain.city.store.entity.City;
import com.b6.mypaldotrip.domain.city.store.repository.CityRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityCreateRes createCity(CityCreateReq req) {

        //유저의 권한이 관리자인지 확인(securityconfig에서 관리자만 할수 있게 하는지)

        //같은 시 명이 중복되는지 확인
        findCity(req.cityName());

        City city = City.builder()
            .provinceName(req.provinceName())
            .cityName(req.cityName())
            .cityInfo(req.cityInfo())
            .build();

        cityRepository.save(city);

        return CityCreateRes.builder()
            .provinceName(city.getProvinceName())
            .cityName(city.getCityName())
            .cityInfo(city.getCityInfo())
            .build();
    }

    public CityUpdateRes updateCity(Long cityId, CityUpdateReq req) {
        //수정하려는 시가 존재하는지 확인
        City city = findCity(cityId);
        //같은 시 명이 중복되는지 확인
        findCity(req.cityName());

        city.update(req.provinceName(), req.cityName(), req.cityInfo());

        return CityUpdateRes
            .builder()
            .provinceName(city.getProvinceName())
            .cityName(city.getCityName())
            .cityInfo(city.getCityInfo())
            .build();
    }

    public CityDeleteRes deleteCity(Long cityId) {
        return null;
    }

    public List<CityListRes> getCityList() {
        return null;
    }

    private City findCity(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(
            () -> new GlobalException(CityErrorCode.CITY_NOT_FOUND)
        );
    }

    private City findCity(String cityName) {
        return cityRepository.findByCityName(cityName).orElseThrow(
            () -> new GlobalException(CityErrorCode.ALREADY_CITY_EXIST)
        );
    }
}
