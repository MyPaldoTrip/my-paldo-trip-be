package com.b6.mypaldotrip.domain.courseFile.controller.dto.response;

import lombok.Builder;

@Builder
public record CourseFileListRes(Long CourseFileId, String FileURL) {

}
