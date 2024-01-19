package com.b6.mypaldotrip.domain.weather.controller;


import com.b6.mypaldotrip.domain.weather.controller.dto.CustomWeatherReq;
import com.b6.mypaldotrip.domain.weather.excepiton.WeatherErrorCode;
import com.b6.mypaldotrip.domain.weather.service.WeatherService;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.response.RestResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/weathers")
public class WeatherController {

    @Value("${weather.service-Key}")
    String serviceKey;

    private WeatherService weatherService;
    private final VersionConfig versionConfig;
    @GetMapping
    public ResponseEntity<RestResponse<String>> weatherAPI(@RequestBody CustomWeatherReq customWeatherReq) {

        //http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=인증키&numOfRows=10&pageNo=1&base_date=20210628&base_time=0500&nx=55&ny=127

        //String res = weatherService.getWeather(customWeatherReq);

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
        String apiUrl = "?serviceKey={serviceKey}&pageNo=1&numOfRows=10&dataType=JSON&base_date={baseDate}&base_time=0500&nx={nx}&ny={ny}";
        String uriString = UriComponentsBuilder.fromUriString(apiUrl)
            .buildAndExpand(serviceKey, base_date, customWeatherReq.nx(), customWeatherReq.ny())
            .toUriString();

        String weatherResponse = webClient.get()
            .uri(uriString)//apiUrl,serviceKey,base_date,customWeatherReq.nx(),customWeatherReq.ny())
            .retrieve()
            .bodyToMono(String.class)
            .block();

        log.info(weatherResponse);
        log.info(uriString);

        if(weatherResponse==null||weatherResponse.isEmpty()){
            throw new GlobalException(WeatherErrorCode.WEATHER_CHECK_FAIL);
        }
    return RestResponse.success(weatherResponse, GlobalResultCode.SUCCESS, versionConfig.getVersion())
        .toResponseEntity();
    }

}
