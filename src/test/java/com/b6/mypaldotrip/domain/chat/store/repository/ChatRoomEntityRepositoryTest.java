package com.b6.mypaldotrip.domain.chat.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
public class ChatRoomEntityRepositoryTest {

    @Mock
    private ChatRoomEntityRepository chatRoomEntityRepository;

    protected String chatRoomId;
    protected String chatRoomName;
    protected ChatRoomEntity chatRoomEntity;

    @BeforeEach
    void setup() {
        chatRoomId = "id1";
        chatRoomName = "chatRoom1";
        chatRoomEntity =
            ChatRoomEntity.builder().chatRoomId(chatRoomId).chatRoomName(chatRoomName).build();
    }

    @Test
    void findByChatRoomIdTest() {
        // when
        when(chatRoomEntityRepository.findByChatRoomId(chatRoomId)).thenReturn(
            Optional.of(chatRoomEntity));

        Optional<ChatRoomEntity> foundEntity =
            chatRoomEntityRepository.findByChatRoomId(chatRoomId);

        // then
        assertTrue(foundEntity.isPresent());
        assertEquals(chatRoomId, foundEntity.get().getChatRoomId());
    }

    @Test
    void findByChatRoomNameTest() {
        // when
        when(chatRoomEntityRepository.findByChatRoomName(chatRoomName)).thenReturn(
            Optional.of(chatRoomEntity));

        Optional<ChatRoomEntity> foundEntity =
            chatRoomEntityRepository.findByChatRoomName(chatRoomName);

        // then
        assertTrue(foundEntity.isPresent());
        assertEquals(chatRoomName, foundEntity.get().getChatRoomName());
    }
}
