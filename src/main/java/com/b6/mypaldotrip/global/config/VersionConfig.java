package com.b6.mypaldotrip.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class VersionConfig {

    @Value("${mpt.version}")
    private String version;
}
