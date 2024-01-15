package com.b6.mypaldotrip.domain.tripFile.controller.dto.response;

import lombok.Builder;

@Builder
public record TripFileListRes(Long tripFileId, String fileUrl) {}
