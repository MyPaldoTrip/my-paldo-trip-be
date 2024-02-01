package com.b6.mypaldotrip.domain.chat;

import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomIdRes;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomInfoRes;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatMessageRepository;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatRoomEntityRepository;
import com.b6.mypaldotrip.domain.user.CommonControllerTest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(MockitoExtension.class)
public abstract class ChatRepositoryTestBase extends CommonControllerTest {

    @MockBean
    protected ChatRoomEntityRepository chatRoomEntityRepository;

    @MockBean
    protected ChatMessageRepository chatMessageRepository;
    protected ChatRoomEntity chatRoom1;
    protected ChatRoomEntity chatRoom2;
    protected String chatRoomId1;
    protected String chatRoomId2;
    protected ChatMessage chatMessage1;
    protected ChatMessage chatMessage2;
    protected ChatMessage chatMessage3;
    protected ChatRoomInfoRes chatRoomInfoRes;

    protected ChatRoomIdRes chatRoomIdRes;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;


    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setup() {
        //given
        chatRoomId1 = "id" + UUID.randomUUID();
        chatRoomId2 = "id" + UUID.randomUUID();
        chatRoom1 = ChatRoomEntity.builder()
            .chatRoomId(chatRoomId1)
            .chatRoomName("마산")
            .build();

        chatRoom2 = ChatRoomEntity.builder()
            .chatRoomId(chatRoomId2)
            .chatRoomName("군산")
            .build();

        // repository에 저장
        chatRoomEntityRepository.save(chatRoom1);
        chatRoomEntityRepository.save(chatRoom2);

        // Mocking 설정
        when(chatRoomEntityRepository.findByChatRoomName(chatRoom1.getChatRoomName())).thenReturn(Optional.of(chatRoom1));
        when(chatRoomEntityRepository.findByChatRoomName(chatRoom2.getChatRoomName())).thenReturn(
            Optional.of(chatRoom2));

        chatMessage1 = ChatMessage.builder()
            .id("1")
            .senderId("첫번째 아이디")
            .recipientId(chatRoom1.getChatRoomId())
            .timestamp(new Date())
            .content("안녕111111")
            .chatRoomId(chatRoom1.getChatRoomId())
            .chatRoomName(chatRoom1.getChatRoomName())
            .build();

        chatMessage2 = ChatMessage.builder()
            .id("2")
            .senderId("두번째 아이디")
            .recipientId(chatRoom1.getChatRoomId())
            .timestamp(new Date())
            .content("안녕222222")
            .chatRoomId(chatRoom1.getChatRoomId())
            .chatRoomName(chatRoom1.getChatRoomName())
            .build();

        chatMessage3 = ChatMessage.builder()
            .id("3")
            .senderId("세번째 아이디")
            .recipientId(chatRoom2.getChatRoomId())
            .timestamp(new Date())
            .content("안녕33333")
            .chatRoomId(chatRoom2.getChatRoomId())
            .chatRoomName(chatRoom2.getChatRoomName())
            .build();

        chatRoomInfoRes = ChatRoomInfoRes.builder()
            .chatMessages(Arrays.asList(chatMessage1, chatMessage2))
            .chatMessageSenders(Arrays.asList(chatMessage1,
                chatMessage2)) // senderId와 chatRoomId가 동일하므로 이렇게 설정했습니다.
            .build();

        chatRoomIdRes = ChatRoomIdRes.builder()
            .chatRoomId(chatRoom2.getChatRoomId())
            .build();

    }

    @AfterEach
    void end() {
//        chatRoomEntityRepository.deleteById(chatRoomId1);
//        chatRoomEntityRepository.deleteById(chatRoomId2);
//
//        chatMessageRepository.deleteById(chatMessage1.getId());
//        chatMessageRepository.deleteById(chatMessage2.getId());
    }


}

