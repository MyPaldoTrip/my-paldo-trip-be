package com.b6.mypaldotrip.domain.chat.service;

import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatMessageRepository;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatRoomEntityRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomEntityRepository chatRoomEntityRepository;

    public ResponseEntity<List<ChatMessage>> findChatMessagesByChatRoomIdAndSenderId(String chatRoomId, String senderId) {

        return ResponseEntity.ok(repository.findByChatRoomIdAndSenderId(chatRoomId, senderId));
    }


    public ResponseEntity<?> createARoom(String chatRoomName) {
        String chatRoomId = UUID.randomUUID().toString();
        ChatRoomEntity chatMessageEntity = ChatRoomEntity.builder()
            .chatRoomId(chatRoomId)
            .chatRoomName(chatRoomName)
            .build();

        chatRoomEntityRepository.save(chatMessageEntity);

        return ResponseEntity.ok(201);
    }


    public List<ChatRoomEntity> getChatRoomList() {
        List<ChatRoomEntity> chatMessages = chatRoomEntityRepository.findAll();

        return chatMessages;
    }

    public ChatMessage findByRoomIdAndSave(String chatRoomId, ChatMessage chatMessage) {
        if(chatRoomEntityRepository.findByChatRoomId(chatRoomId).isPresent()) {

            repository.save(chatMessage);
        }
        return chatMessage;
    }

    public ResponseEntity<List<ChatMessage>> findAllMessagesByChatRoomId(String chatRoomId) {
        return ResponseEntity.ok(repository.findAllByChatRoomId(chatRoomId));
    }
}
