package com.b6.mypaldotrip.domain.city.s3.dto;

import lombok.Builder;

@Builder
public record CItyFileListRes(Long Id, String FileURL) {}
