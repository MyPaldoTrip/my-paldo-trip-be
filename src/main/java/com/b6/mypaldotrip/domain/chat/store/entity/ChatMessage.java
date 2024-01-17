package com.b6.mypaldotrip.domain.chat.store.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {

    @Id
    private String id;
    private String chatRoomId;
    private String chatRoomName;
    private String senderId;
    private String recipientId;
    private String content;
    private Date timestamp;
}
