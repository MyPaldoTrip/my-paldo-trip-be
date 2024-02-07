package com.b6.mypaldotrip.domain.city.service;

import com.b6.mypaldotrip.domain.city.store.entity.CityEntity;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;

public interface CityTest {
    String TEST_USERNAME = "name";
    String TEST_PASSWORD = "password";
    String TEST_EMAIL = "test@gmail.com";
    String TEST_INTRODUCTION = "test introduction";
    String TEST_FILE_URL =
            "https://my-mpt-bucket.s3.ap-northeast-2.amazonaws.com/user/e82283cc-fa55-4cd0-9a61-a2436acbf746.png";
    Long TEST_AGE = 20L;
    Long TEST_LEVEL = 1L;
    Long TEST_CITYID = 1L;
    String TEST_PROVINCE_NAME = "test province";
    String TEST_CITY_NAME = "test city name";
    String TEST_CITY_INFO = "test city info";

    CityEntity TEST_CITY =
            CityEntity.builder()
                    .provinceName(TEST_PROVINCE_NAME)
                    .cityName(TEST_CITY_NAME)
                    .cityInfo(TEST_CITY_INFO)
                    .build();
    UserEntity TEST_USER =
            UserEntity.builder()
                    .username(TEST_USERNAME)
                    .email(TEST_EMAIL)
                    .password(TEST_PASSWORD)
                    .introduction(TEST_INTRODUCTION)
                    .fileURL(TEST_FILE_URL)
                    .age(TEST_AGE)
                    .level(TEST_LEVEL)
                    .build();
}
