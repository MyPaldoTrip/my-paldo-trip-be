package com.b6.mypaldotrip.global.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
public class MetaData {

    private final LocalDateTime requestTime;

    private final String apiVersion;

    @Builder
    private MetaData(String apiVersion) {
        this.apiVersion = apiVersion;
        this.requestTime = LocalDateTime.now();
    }
}
