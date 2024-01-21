package com.b6.mypaldotrip.domain.chat.controller.dto.response;

import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomInfoRes(
        List<ChatMessage> chatMessageSenders, List<ChatMessage> chatMessages) {}
