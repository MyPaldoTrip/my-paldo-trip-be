package com.b6.mypaldotrip.domain.weather.controller;

import com.b6.mypaldotrip.domain.weather.controller.dto.CustomOpenWeatherReq;
import com.b6.mypaldotrip.domain.weather.controller.dto.CustomOpenWeatherRes;
import com.b6.mypaldotrip.domain.weather.controller.dto.OpenWeatherRes;
import com.b6.mypaldotrip.domain.weather.service.WeatherService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.response.RestResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/weathers")
public class WeatherController {

    private final WeatherService weatherService;
    private final VersionConfig versionConfig;

    @PostMapping("/open")
    public ResponseEntity<RestResponse<List<CustomOpenWeatherRes>>> openWeatherAPI(
            @RequestBody CustomOpenWeatherReq openWeatherReq) {

        OpenWeatherRes res = weatherService.getOpenWeather(openWeatherReq);
        List<CustomOpenWeatherRes> customWeatherList = weatherService.processWeatherData(res);
        return ResponseEntity.ok(
                RestResponse.success(
                        customWeatherList, GlobalResultCode.SUCCESS, versionConfig.getVersion()));
    }
}
