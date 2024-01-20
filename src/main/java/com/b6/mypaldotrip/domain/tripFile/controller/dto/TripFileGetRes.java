package com.b6.mypaldotrip.domain.tripFile.controller.dto;

import lombok.Builder;

@Builder
public record TripFileGetRes(Long tripFileId, String fileUrl) {}
