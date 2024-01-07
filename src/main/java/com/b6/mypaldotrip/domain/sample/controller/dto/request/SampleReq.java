package com.b6.mypaldotrip.domain.sample.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SampleReq(@NotBlank(message = "title이 비었습니다.") String title,
                        @NotBlank(message = "content가 비었습니다.") String content) {

}
