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
import com.b6.mypaldotrip.domain.course.controller.dto.response.CourseSaveRes;
import com.b6.mypaldotrip.domain.course.exception.CourseErrorCode;
import com.b6.mypaldotrip.domain.course.store.entity.CourseEntity;
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

        //같은 도시명이 존재하는지 확인
        if(cityRepository.findByCityName(req.cityName()).isPresent()){
            throw new GlobalException(CityErrorCode.ALREADY_EXIST_CITY);
        }

        City city = City.builder()
            .cityName(req.cityName())
            .cityInfo(req.cityInfo())
            .build();

        cityRepository.save(city);

        return CityCreateRes.builder()
            .cityName(city.getCityName())
            .cityInfo(city.getCityInfo())
            .build();
    }

    public CityUpdateRes updateCity(CityUpdateReq cityUpdateReq) {
        return null;
    }

    public CityDeleteRes deleteCity(Long cityId) {
        return null;
    }

    public List<CityListRes> getCityList() {
        return null;
    }
}
