package com.b6.mypaldotrip.domain.chat.service;

import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomInfoRes;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomSaveRes;
import com.b6.mypaldotrip.domain.chat.exception.ChatErrorCode;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatMessageRepository;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatRoomEntityRepository;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomEntityRepository chatRoomEntityRepository;

    public ChatRoomSaveRes createARoom(String chatRoomName) {
        String chatRoomId = "id" + UUID.randomUUID();
        ChatRoomEntity chatMessageEntity =
                ChatRoomEntity.builder().chatRoomId(chatRoomId).chatRoomName(chatRoomName).build();

        chatRoomEntityRepository.save(chatMessageEntity);

        return ChatRoomSaveRes.builder().content(chatMessageEntity.getChatRoomName()).build();
    }

    public List<ChatRoomEntity> getChatRoomList() {
        List<ChatRoomEntity> chatRoomList = chatRoomEntityRepository.findAll();

        return chatRoomList;
    }

    public ChatMessage saveMessageIfRoomExists(String chatRoomId, ChatMessage chatMessage) {
        if (chatRoomEntityRepository.findByChatRoomId(chatRoomId).isPresent()) {

            chatMessageRepository.save(chatMessage);
        }
        return chatMessage;
    }

    public ChatRoomInfoRes findAllMessagesByChatRoomId(String chatRoomId) {
        List<ChatMessage> chatMessageSenders = chatMessageRepository.findAllBySenderId(chatRoomId);
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);

        return ChatRoomInfoRes.builder()
                .chatMessages(chatMessages)
                .chatMessageSenders(chatMessageSenders)
                .build();
    }

    public ChatRoomEntity updateChatRoom(String chatRoomName, String updateRoomName) {
        ChatRoomEntity chatRoomEntity =
                chatRoomEntityRepository
                        .findByChatRoomName(chatRoomName)
                        .orElseThrow(() -> new GlobalException(ChatErrorCode.CHATROOM_NOT_FOUND));
        chatRoomEntity.updateChatRoomName(updateRoomName);
        chatRoomEntityRepository.save(chatRoomEntity);
        return chatRoomEntity;
    }

    public ChatRoomEntity deleteChatRoom(String chatRoomName) {
        ChatRoomEntity chatRoomEntity =
                chatRoomEntityRepository
                        .findByChatRoomName(chatRoomName)
                        .orElseThrow(() -> new GlobalException(ChatErrorCode.CHATROOM_NOT_FOUND));

        chatRoomEntityRepository.delete(chatRoomEntity);
        return chatRoomEntity;
    }
}
