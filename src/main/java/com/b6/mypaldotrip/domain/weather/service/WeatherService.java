package com.b6.mypaldotrip.domain.weather.service;

import com.b6.mypaldotrip.domain.weather.controller.dto.CustomOpenWeatherReq;
import com.b6.mypaldotrip.domain.weather.controller.dto.CustomOpenWeatherRes;
import com.b6.mypaldotrip.domain.weather.controller.dto.OpenWeatherRes;
import com.b6.mypaldotrip.domain.weather.controller.dto.OpenWeatherRes.WeatherItem;
import com.b6.mypaldotrip.domain.weather.store.CityMapping;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
public class WeatherService {

    @Value("${weather.api-key}")
    String apiKey;

    // @Cacheable(value = "weatherCache", key = "{#root.methodName, #openWeatherReq.cityName}")
    public OpenWeatherRes getOpenWeather(CustomOpenWeatherReq openWeatherReq) {

        String changeCityName = CityMapping.convertToEnglish(openWeatherReq.cityName());

        String url = "https://api.openweathermap.org/data/2.5/forecast";
        WebClient webClient = WebClient.builder().baseUrl(url).build();

        String apiUrl = "?q={cityName}&appid={APIkey}&units=metric";
        String uriString =
                UriComponentsBuilder.fromUriString(apiUrl)
                        .buildAndExpand(changeCityName, apiKey)
                        .toUriString();

        OpenWeatherRes weatherResponse =
                webClient.get().uri(uriString).retrieve().bodyToMono(OpenWeatherRes.class).block();

        List<WeatherItem> weatherfilteredList = Collections.emptyList();

        if (weatherResponse != null && weatherResponse.list() != null) {
            weatherfilteredList = weatherResponse.list().stream().toList();
        }
        return new OpenWeatherRes(weatherfilteredList);
    }

    public List<CustomOpenWeatherRes> processWeatherData(OpenWeatherRes weatherResponse) {
        Map<String, CustomOpenWeatherRes> resultMap = new LinkedHashMap<>();

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (OpenWeatherRes.WeatherItem item : weatherResponse.list()) {
            LocalDateTime dateTime = LocalDateTime.parse(item.date(), inputFormatter);
            String formattedDate = dateTime.format(outputFormatter);

            resultMap.compute(
                    formattedDate,
                    (key, existingValue) -> {
                        if (existingValue == null) {
                            return new CustomOpenWeatherRes(
                                    formattedDate,
                                    item.weather().get(0).main(),
                                    item.main().temp_max(),
                                    item.main().temp_min());
                        } else {
                            // 이미 있는 경우, 최고 기온과 최저 기온을 갱신
                            double maxTemp =
                                    Math.max(existingValue.max_temp(), item.main().temp_max());
                            double minTemp =
                                    Math.min(existingValue.min_temp(), item.main().temp_min());

                            return new CustomOpenWeatherRes(
                                    formattedDate, item.weather().get(0).main(), maxTemp, minTemp);
                        }
                    });
        }

        return new ArrayList<>(resultMap.values());
    }

    @CacheEvict(value = "weatherCache", allEntries = true)
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // 6시간마다 실행
    public void deleteWeatherCache() {}
}
