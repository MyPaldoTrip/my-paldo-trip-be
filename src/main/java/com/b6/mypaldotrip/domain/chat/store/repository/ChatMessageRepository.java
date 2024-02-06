package com.b6.mypaldotrip.domain.chat.store.repository;

import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByChatRoomId(String chatRoomId);

    List<ChatMessage> findAllBySenderId(String senderId);
}
