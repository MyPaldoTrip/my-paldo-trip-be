package com.b6.mypaldotrip.domain.chat.controller.dto.response;

import lombok.Builder;

@Builder
public record ChatRoomSaveRes(
    String content
) {

}
