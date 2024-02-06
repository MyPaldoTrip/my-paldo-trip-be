package com.b6.mypaldotrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MypaldotripApplication {

    public static void main(String[] args) {
        SpringApplication.run(MypaldotripApplication.class, args);
    }
}
