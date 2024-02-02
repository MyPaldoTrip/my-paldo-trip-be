package com.b6.mypaldotrip.domain.chat.store.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
public class ChatMessageRepositoryTest {

    @Mock private ChatMessageRepository chatMessageRepository;

    @Autowired private MongoTemplate mongoTemplate;
    protected String chatRoomId1;

    protected String chatRoomId2;

    @AfterEach
    void cleanup() {
        mongoTemplate.remove(
                Query.query(Criteria.where("chatRoomId").is(chatRoomId1)), ChatMessage.class);
        mongoTemplate.remove(
                Query.query(Criteria.where("chatRoomId").is(chatRoomId2)), ChatMessage.class);
    }

    @Test
    void findAllMessagesByChatRoomIdSuccessTest() {
        // given
        chatRoomId1 = "id1";
        ChatMessage chatMessage1 =
                ChatMessage.builder()
                        .chatRoomId(chatRoomId1)
                        .senderId("user1")
                        .content("Hello, Test!")
                        .timestamp(new Date())
                        .build();
        ChatMessage chatMessage2 =
                ChatMessage.builder()
                        .chatRoomId(chatRoomId1)
                        .senderId("user2")
                        .content("Hello,Test!")
                        .timestamp(new Date())
                        .build();
        List<ChatMessage> chatMessages = Arrays.asList(chatMessage1, chatMessage2);

        // when
        when(chatMessageRepository.findAllByChatRoomId(chatRoomId1)).thenReturn(chatMessages);

        List<ChatMessage> result = chatMessageRepository.findAllByChatRoomId(chatRoomId1);

        // then
        assertEquals(2, result.size());
        assertThat(result)
                .usingElementComparatorIgnoringFields("id", "timestamp")
                .contains(chatMessage1, chatMessage2);
    }

    @Test
    void findAllBySenderIdTest() {
        String senderId = "user1";
        chatRoomId1 = "id1";
        chatRoomId2 = "id2";
        ChatMessage chatMessage1 =
                ChatMessage.builder()
                        .chatRoomId(chatRoomId1)
                        .senderId(senderId)
                        .content("Hello")
                        .timestamp(new Date())
                        .build();
        ChatMessage chatMessage2 =
                ChatMessage.builder()
                        .chatRoomId(chatRoomId2)
                        .senderId(senderId)
                        .content("Hi")
                        .timestamp(new Date())
                        .build();

        List<ChatMessage> mockMessages = Arrays.asList(chatMessage1, chatMessage2);

        // when chatMessageRepository.findAllBySenderId 호출되면 mockMessages를 반환하도록 설정
        when(chatMessageRepository.findAllBySenderId(senderId)).thenReturn(mockMessages);

        List<ChatMessage> foundMessages = chatMessageRepository.findAllBySenderId(senderId);

        assertEquals(2, foundMessages.size());
        assertThat(foundMessages)
                .usingElementComparatorIgnoringFields("id", "timestamp")
                .contains(chatMessage1, chatMessage2);
    }
}
