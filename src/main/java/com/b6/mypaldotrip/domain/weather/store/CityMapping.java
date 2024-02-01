package com.b6.mypaldotrip.domain.weather.store;

import com.b6.mypaldotrip.domain.weather.exception.WeatherErrorCode;
import com.b6.mypaldotrip.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CityMapping {
    SEOUL("서울", "seoul"),
    BUSAN("부산", "busan"),
    INCHEON("인천", "incheon"),
    DAEGU("대구", "daegu"),
    JEJU("제주도", "jeju"),
    GWANGJU("광주", "Gwangju"),
    DAEJEON("대전", "Daejeon "),
    GYEONGGI("경기", "Gyeonggi-do"),
    CHUNGNAM("충남", "Chungcheongnam-do"),
    CHUNGBUK("충북", "Chungcheongbuk-do"),
    JEOLLANAM("전라남도", "Jeollanam-do"),
    JEOLLABUK("전라북도", "Jeollabuk-do"),
    GYEONGSANGNAM("경상남도", "Gyeongsangnam-do"),
    GYEONGSANGBUK("경상북도", "Gyeongsangbuk-do");

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
