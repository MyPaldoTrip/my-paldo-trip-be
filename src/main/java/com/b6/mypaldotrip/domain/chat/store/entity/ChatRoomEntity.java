package com.b6.mypaldotrip.domain.chat.store.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatRoomEntity {
    @Id
    private String chatRoomId;

    private String chatRoomName;

    public void updateChatRoomName(String updateRoomName) {
        chatRoomName = updateRoomName;
    }
}

