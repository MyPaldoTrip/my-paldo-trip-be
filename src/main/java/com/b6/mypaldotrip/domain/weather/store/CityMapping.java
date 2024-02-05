package com.b6.mypaldotrip.domain.weather.store;

import com.b6.mypaldotrip.domain.weather.exception.WeatherErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CityMapping {
    SEOUL("서울특별시", "seoul"),
    INCHEON("인천광역시", "incheon"),
    BUSAN("부산광역시", "busan"),
    DAEGU("대구광역시", "daegu"),
    GWANGJU("광주광역시", "Gwangju"),
    ULSAN("울산광역시", "Ulsan"),
    JEJU("제주도", "jeju"),
    DAEJEON("대전광역시", "Daejeon "),
    GYEONGGI("경기도", "Gyeonggi-do"),
    GANGWON("강원도", "Gangwon-do"),
    CHUNGNAM("충청도", "Chungcheongnam-do"),
    JEOLLANAM("전라도", "Jeollanam-do"),
    GYEONGSANGNAM("경상도", "Gyeongsangnam-do");

    private final String beforeCityName;
    private final String changeCityName;

    public static String convertToEnglish(String koreanName) {
        for (CityMapping city : values()) {
            if (city.beforeCityName.equals(koreanName)) {
                return city.changeCityName;
            }
        }
        throw new GlobalException(WeatherErrorCode.WEATHER_CITY_NOT_FOUND);
    }
}
