package com.b6.mypaldotrip.domain.chat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomIdRes;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomInfoRes;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomSaveRes;
import com.b6.mypaldotrip.domain.chat.exception.ChatErrorCode;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatMessageRepository;
import com.b6.mypaldotrip.domain.chat.store.repository.ChatRoomEntityRepository;
import com.b6.mypaldotrip.domain.user.CommonTest;
import com.b6.mypaldotrip.global.exception.GlobalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest implements CommonTest {

    @InjectMocks private ChatMessageService chatMessageService;

    @Mock private ChatMessageRepository chatMessageRepository;

    @Mock private ChatRoomEntityRepository chatRoomEntityRepository;

    @Mock private MongoTemplate mongoTemplate;

    @Nested
    @DisplayName("채팅방 생성")
    class 채팅방_생성 {

        @Test
        @DisplayName("채팅방 생성 : 성공")
        void createARoomTest() {
            // given
            String chatRoomName = "testRoom";
            String chatRoomId = "id" + UUID.randomUUID();
            ChatRoomEntity chatMessageEntity =
                    ChatRoomEntity.builder()
                            .chatRoomId(chatRoomId)
                            .chatRoomName(chatRoomName)
                            .build();

            when(chatRoomEntityRepository.save(any(ChatRoomEntity.class)))
                    .thenReturn(chatMessageEntity);

            // when
            ChatRoomSaveRes result = chatMessageService.createARoom(chatRoomName);

            // then
            assertEquals(result.content(), chatRoomName);
        }

        @Test
        @DisplayName("채팅방 생성 : 실패")
        void createARoomFailTest() {
            // given
            String chatRoomName = "testRoom";

            when(chatRoomEntityRepository.save(any(ChatRoomEntity.class)))
                    .thenThrow(new RuntimeException("DB 에러"));

            // when
            Exception exception =
                    assertThrows(
                            RuntimeException.class,
                            () -> {
                                chatMessageService.createARoom(chatRoomName);
                            });

            // then
            assertEquals("DB 에러", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("채팅방 목록 가져오기 테스트")
    class 채팅방_목록_받기 {
        @Test
        @DisplayName("채팅방 목록 가져오기 테스트 : 성공")
        void getChatRoomListTest() {
            // given
            List<ChatRoomEntity> expectedChatRoomList = new ArrayList<>();
            expectedChatRoomList.add(
                    ChatRoomEntity.builder().chatRoomId("id1").chatRoomName("room1").build());
            expectedChatRoomList.add(
                    ChatRoomEntity.builder().chatRoomId("id2").chatRoomName("room2").build());

            Query query = new Query();
            query.with(Sort.by(Sort.Direction.ASC, "chatRoomName"));

            when(mongoTemplate.find(query, ChatRoomEntity.class)).thenReturn(expectedChatRoomList);

            // when
            List<ChatRoomEntity> resultChatRoomList = chatMessageService.getChatRoomList();

            // then
            assertEquals(expectedChatRoomList, resultChatRoomList);
        }

        @Test
        @DisplayName("채팅방 목록 가져오기 테스트 : 실패")
        void getChatRoomListFailTest() {
            // given
            Query query = new Query();
            query.with(Sort.by(Sort.Direction.ASC, "chatRoomName"));

            when(mongoTemplate.find(query, ChatRoomEntity.class))
                    .thenThrow(new RuntimeException("DB 에러"));

            // when
            Exception exception =
                    assertThrows(
                            RuntimeException.class,
                            () -> {
                                chatMessageService.getChatRoomList();
                            });

            // then
            assertEquals("DB 에러", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("채팅방 존재하면 채팅 저장 테스트")
    class 채팅방_존재하면_채팅_저장 {

        @Test
        @DisplayName("채팅방이 존재할 때 메시지 저장 테스트 : 성공")
        void saveMessageIfRoomExistsTest() {
            // given
            String chatRoomId = "id1";
            ChatMessage chatMessage = ChatMessage.builder().content("Hello, Test!").build();
            ChatRoomEntity chatRoomEntity =
                    ChatRoomEntity.builder().chatRoomId(chatRoomId).chatRoomName("room1").build();

            when(chatRoomEntityRepository.findByChatRoomId(chatRoomId))
                    .thenReturn(Optional.of(chatRoomEntity));
            when(chatMessageRepository.save(chatMessage)).thenReturn(chatMessage);

            // when
            ChatMessage resultMessage =
                    chatMessageService.saveMessageIfRoomExists(chatRoomId, chatMessage);

            // then
            assertEquals(chatMessage, resultMessage);
        }

        @Test
        @DisplayName("채팅방이 존재하지 않을 때 메시지 저장 테스트 : 실패")
        void saveMessageIfRoomDoesntExistTest() {
            // given
            String chatRoomId = "id1";
            ChatMessage chatMessage = ChatMessage.builder().content("Hello, Test!").build();

            when(chatRoomEntityRepository.findByChatRoomId(chatRoomId))
                    .thenReturn(Optional.empty());

            // when
            ChatMessage resultMessage =
                    chatMessageService.saveMessageIfRoomExists(chatRoomId, chatMessage);

            // then
            assertNull(resultMessage);
        }
    }

    @Nested
    @DisplayName("채팅방 아이디로 모든 메시지 찾기 테스트")
    class 채팅방의_모든_메시지_불러오기 {

        @Test
        @DisplayName("채팅방 아이디로 모든 메시지 찾기 테스트 : 성공")
        void findAllMessagesByChatRoomIdTest() {
            // given
            String chatRoomId = "id1";
            List<ChatMessage> chatMessageSenders =
                    Arrays.asList(
                            ChatMessage.builder()
                                    .id("1")
                                    .chatRoomId(chatRoomId)
                                    .senderId("sender1")
                                    .content("Hello, Test1!")
                                    .timestamp(new Date())
                                    .build(),
                            ChatMessage.builder()
                                    .id("2")
                                    .chatRoomId(chatRoomId)
                                    .senderId("sender2")
                                    .content("Hello, Test2!")
                                    .timestamp(new Date())
                                    .build());
            List<ChatMessage> chatMessages =
                    Arrays.asList(
                            ChatMessage.builder()
                                    .id("3")
                                    .chatRoomId(chatRoomId)
                                    .senderId("sender3")
                                    .content("Hello, Test3!")
                                    .timestamp(new Date())
                                    .build(),
                            ChatMessage.builder()
                                    .id("4")
                                    .chatRoomId(chatRoomId)
                                    .senderId("sender4")
                                    .content("Hello, Test4!")
                                    .timestamp(new Date())
                                    .build());

            when(chatMessageRepository.findAllBySenderId(chatRoomId))
                    .thenReturn(chatMessageSenders);
            when(chatMessageRepository.findAllByChatRoomId(chatRoomId)).thenReturn(chatMessages);

            // when
            ChatRoomInfoRes result = chatMessageService.findAllMessagesByChatRoomId(chatRoomId);

            // then
            assertEquals(chatMessageSenders, result.chatMessageSenders());
            assertEquals(chatMessages, result.chatMessages());
        }

        @Test
        @DisplayName("채팅방 아이디로 모든 메시지 찾기 테스트 : 실패")
        void findAllMessagesByChatRoomIdFailTest() {
            // given
            String chatRoomId = "id1";

            when(chatMessageRepository.findAllBySenderId(chatRoomId))
                    .thenThrow(new RuntimeException("DB 에러"));

            // when
            Exception exception =
                    assertThrows(
                            RuntimeException.class,
                            () -> {
                                chatMessageService.findAllMessagesByChatRoomId(chatRoomId);
                            });

            // then
            assertEquals("DB 에러", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("채팅방 이름 업데이트 테스트")
    class 채팅방_이름_업데이트_테스트 {
        @Test
        @DisplayName("채팅방 이름 업데이트 테스트 : 성공")
        void updateChatRoomTest() {
            // given
            String chatRoomName = "room1";
            String updateRoomName = "room1_updated";
            ChatRoomEntity chatRoomEntity =
                    ChatRoomEntity.builder().chatRoomId("id1").chatRoomName(chatRoomName).build();

            when(chatRoomEntityRepository.findByChatRoomName(chatRoomName))
                    .thenReturn(Optional.of(chatRoomEntity));
            when(chatRoomEntityRepository.save(chatRoomEntity)).thenReturn(chatRoomEntity);

            // when
            ChatRoomEntity resultEntity =
                    chatMessageService.updateChatRoom(chatRoomName, updateRoomName);

            // then
            assertEquals(updateRoomName, resultEntity.getChatRoomName());
        }

        @Test
        @DisplayName("채팅방 이름 업데이트 테스트 : 실패 (채팅방 없음)")
        void updateChatRoomFailTest() {
            // given
            String chatRoomName = "room1";
            String updateRoomName = "room1_updated";

            when(chatRoomEntityRepository.findByChatRoomName(chatRoomName))
                    .thenReturn(Optional.empty());

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                chatMessageService.updateChatRoom(chatRoomName, updateRoomName);
                            });

            // then
            assertEquals(ChatErrorCode.CHATROOM_NOT_FOUND, exception.getResultCode());
        }
    }

    @Nested
    @DisplayName("채팅방 삭제 테스트")
    class 채팅방_삭제_테스트 {
        @Test
        @DisplayName("채팅방 삭제 테스트 : 성공")
        void deleteChatRoomTest() {
            // given
            String chatRoomName = "room1";
            ChatRoomEntity chatRoomEntity =
                    ChatRoomEntity.builder().chatRoomId("id1").chatRoomName(chatRoomName).build();

            when(chatRoomEntityRepository.findByChatRoomName(chatRoomName))
                    .thenReturn(Optional.of(chatRoomEntity));
            doNothing().when(chatRoomEntityRepository).delete(chatRoomEntity);

            // when
            ChatRoomEntity resultEntity = chatMessageService.deleteChatRoom(chatRoomName);

            // then
            assertEquals(chatRoomEntity, resultEntity);
        }

        @Test
        @DisplayName("채팅방 삭제 테스트 : 실패 (채팅방 없음)")
        void deleteChatRoomFailTest() {
            // given
            String chatRoomName = "room1";

            when(chatRoomEntityRepository.findByChatRoomName(chatRoomName))
                    .thenReturn(Optional.empty());

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                chatMessageService.deleteChatRoom(chatRoomName);
                            });

            // then
            assertEquals(ChatErrorCode.CHATROOM_NOT_FOUND, exception.getResultCode());
        }
    }

    @Nested
    @DisplayName("채팅방 이름 검증 테스트")
    class 채팅방_이름_검증_테스트 {

        @Test
        @DisplayName("채팅방 이름 검증 테스트 : 성공")
        void validateChatRoomNameTest() {
            // given
            String checkRoomNameSame = "room1";

            when(chatRoomEntityRepository.findByChatRoomName(checkRoomNameSame))
                    .thenReturn(Optional.empty());

            // when
            String resultName = chatMessageService.validateChatRoomName(checkRoomNameSame);

            // then
            assertEquals(checkRoomNameSame, resultName);
        }

        @Test
        @DisplayName("채팅방 이름 검증 테스트 : 실패 (채팅방 이미 존재)")
        void validateChatRoomNameFailTest() {
            // given
            String checkRoomNameSame = "room1";
            ChatRoomEntity chatRoomEntity =
                    ChatRoomEntity.builder()
                            .chatRoomId("id1")
                            .chatRoomName(checkRoomNameSame)
                            .build();

            when(chatRoomEntityRepository.findByChatRoomName(checkRoomNameSame))
                    .thenReturn(Optional.of(chatRoomEntity));

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                chatMessageService.validateChatRoomName(checkRoomNameSame);
                            });

            // then
            assertEquals(ChatErrorCode.CHATROOM_ALREADY_EXISTS, exception.getResultCode());
        }
    }

    @Nested
    @DisplayName("채팅방 id로 방이름 얻기 테스트")
    class 채팅방_id_방이름_얻기_테스트 {
        @Test
        @DisplayName("채팅방 아이디 조회 테스트 : 성공")
        void getChatRoomIdByChatRoomNameTest() {
            // given
            String chatRoomName = "room1";
            String chatRoomId = "id1";
            ChatRoomEntity chatRoomEntity =
                    ChatRoomEntity.builder()
                            .chatRoomId(chatRoomId)
                            .chatRoomName(chatRoomName)
                            .build();

            when(chatRoomEntityRepository.findByChatRoomName(chatRoomName))
                    .thenReturn(Optional.of(chatRoomEntity));

            // when
            ChatRoomIdRes result = chatMessageService.getChatRoomIdByChatRoomName(chatRoomName);

            // then
            assertEquals(chatRoomId, result.chatRoomId());
        }

        @Test
        @DisplayName("채팅방 아이디 조회 테스트 : 실패 (채팅방 없음)")
        void getChatRoomIdByChatRoomNameFailTest() {
            // given
            String chatRoomName = "room1";

            when(chatRoomEntityRepository.findByChatRoomName(chatRoomName))
                    .thenReturn(Optional.empty());

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                chatMessageService.getChatRoomIdByChatRoomName(chatRoomName);
                            });

            // then
            assertEquals(ChatErrorCode.CHATROOM_NOT_FOUND, exception.getResultCode());
        }
    }
}
