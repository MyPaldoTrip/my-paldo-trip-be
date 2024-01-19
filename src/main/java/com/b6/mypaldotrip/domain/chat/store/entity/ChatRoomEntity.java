package com.b6.mypaldotrip.domain.chat.store.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@Builder
@Document
public class ChatRoomEntity {

    @Id private String chatRoomId;

    private String chatRoomName;

    private ChatRoomEntity(){}
    public void updateChatRoomName(String updateRoomName) {
        chatRoomName = updateRoomName;
    }
}
