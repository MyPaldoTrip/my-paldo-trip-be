package com.b6.mypaldotrip.domain.user;

import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;

public interface CommonTest {

    String TEST_USERNAME = "name";
    String TEST_PASSWORD = "password";
    String TEST_EMAIL = "test@gmail.com";
    String TEST_INTRODUCTION = "test introduction";
    String TEST_FILE_URL =
            "https://my-mpt-bucket.s3.ap-northeast-2.amazonaws.com/user/e82283cc-fa55-4cd0-9a61-a2436acbf746.png";
    Long TEST_AGE = 20L;
    Long TEST_LEVEL = 1L;
    String ANOTHER = "another";
    Long ANOTHER_LONG = 1L;

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

    UserEntity TEST_ANOTHER_USER =
            UserEntity.builder()
                    .username(ANOTHER + TEST_USERNAME)
                    .email(ANOTHER + TEST_EMAIL)
                    .password(ANOTHER + TEST_PASSWORD)
                    .introduction(ANOTHER + TEST_INTRODUCTION)
                    .fileURL(ANOTHER + TEST_FILE_URL)
                    .age(ANOTHER_LONG + TEST_AGE)
                    .level(ANOTHER_LONG + TEST_LEVEL)
                    .build();
}
