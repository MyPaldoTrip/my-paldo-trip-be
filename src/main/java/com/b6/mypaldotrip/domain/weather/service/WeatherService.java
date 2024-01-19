package com.b6.mypaldotrip.domain.weather.service;

import com.b6.mypaldotrip.domain.weather.controller.dto.CustomWeatherReq;
import com.b6.mypaldotrip.domain.weather.excepiton.WeatherErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Slf4j
@Service
public class WeatherService {

    @Value("${weather.service-Key}")
    String serviceKey;

    public String getWeather(CustomWeatherReq customWeatherReq) {
        // ?serviceKey=인증키
        // &pageNo=1
        // &numOfRows=809
        // dataType=JSON
        // &base_date=20210628 (현재 날짜로)
        // &base_time=0500
        // &nx=55
        // &ny=127

        //현재 날짜 가져오기
        String base_date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        WebClient webClient = WebClient.builder()
            .baseUrl(url)
            .build();

        String apiUrl = "?serviceKey={serviceKey}&pageNo=1&numOfRows=10&dataType=JSON&base_date={baseDate}8&base_time=0500&nx={nx}&ny={ny}";
        String weatherResponse = webClient.get()
            .uri(apiUrl,serviceKey,base_date,customWeatherReq.nx(),customWeatherReq.ny())
            .retrieve()
            .bodyToMono(String.class)
            .block();
        log.info(weatherResponse);

        if(weatherResponse==null||weatherResponse.isEmpty()){
            throw new GlobalException(WeatherErrorCode.WEATHER_CHECK_FAIL);
        }
    return weatherResponse;
    }
}
