package com.b6.mypaldotrip.domain.chat.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.b6.mypaldotrip.domain.chat.ChatRepositoryTestBase;
import com.b6.mypaldotrip.domain.chat.controller.ChatController;
import com.b6.mypaldotrip.domain.chat.controller.dto.request.CreateRoomReq;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoleRes;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomSaveRes;
import com.b6.mypaldotrip.domain.chat.service.ChatMessageService;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import com.b6.mypaldotrip.global.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WithMockUser
@WebMvcTest(ChatController.class)
public class ChatControllerTest extends ChatRepositoryTestBase {

    @Autowired
    private MockMvc mockMvc;
    //    @MockBean
//    private VersionConfig versionConfig;
    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private ChatMessageService chatMessageService;

    @Test
    @DisplayName("채팅방 모든 메시지 불러오기")
    public void testFindAllMessages() throws Exception {
        // 가짜 토큰
        String fakeToken = "FAKE_TOKEN";

        // createToken 메서드가 호출되면 가짜 토큰 반환
        when(chatMessageService.findAllMessagesByChatRoomId(chatRoom1.getChatRoomId())).thenReturn(
            chatRoomInfoRes);
        when(versionConfig.getVersion()).thenReturn("v1");
        when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);

        MvcResult result = mockMvc.perform(
                get("/api/" + versionConfig.getVersion() + "/chat-rooms/{chatRoomId}",
                    chatRoom1.getChatRoomId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", fakeToken)) // 인증 토큰 제공
            .andExpect(status().isOk())
            .andDo(document(
                "chat-rooms/{chatRoomId}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .andReturn();

    }

    @Test
    @DisplayName("방 이름으로 채팅방 찾기")
    public void testSearchChatRoom() throws Exception {
        // 가짜 토큰
        String fakeToken = "FAKE_TOKEN";

        when(
            chatMessageService.getChatRoomIdByChatRoomName(chatRoom2.getChatRoomName())).thenReturn(
            chatRoomIdRes);
        when(versionConfig.getVersion()).thenReturn("v1");
        when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);

        MvcResult result = mockMvc.perform(
                get("/api/" + versionConfig.getVersion() + "/chat-rooms/search/{chatRoomName}",
                    chatRoom2.getChatRoomName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", fakeToken)) // 인증 토큰 제공
            .andExpect(status().isOk())
            .andDo(document(
                "chat-rooms/search/{ChatRoomName}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .andReturn();

    }

    @Nested
    @DisplayName("채팅방 만들기 테스트")
    class 채팅방_만뜰기 {

        private final String SAMCHEOK = "삼척";
        private final String MASAN = "마산";
        private CreateRoomReq createRoomReq1;
        private CreateRoomReq createRoomReq2;
        private ChatRoomSaveRes chatRoomSaveRes1;
        private ChatRoomSaveRes chatRoomSaveRes2;

        @BeforeEach
        void setup() {

            createRoomReq1 = new CreateRoomReq(SAMCHEOK);
            String validatedChatRoomName1 = chatMessageService.validateChatRoomName(
                createRoomReq1.chatRoomName());
            chatRoomSaveRes1 = chatMessageService.createARoom(validatedChatRoomName1);

            createRoomReq2 = new CreateRoomReq(MASAN);
            chatRoomSaveRes2 = chatMessageService.createARoom(createRoomReq2.chatRoomName());


        }

        @Test
        @DisplayName("채팅방 만들기 테스트 : 성공")
        void 채팅방_만들기_성공() throws Exception {
            // given
            // 가짜 토큰
            String fakeToken = "FAKE_TOKEN";

            when(chatMessageService.createARoom(createRoomReq1.chatRoomName())).thenReturn(
                chatRoomSaveRes1);
            when(versionConfig.getVersion()).thenReturn("v1");
            when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);

            MvcResult result = mockMvc.perform(
                    post("/api/" + versionConfig.getVersion() + "/chat-rooms/rooms", createRoomReq1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createRoomReq1))
                        .header("Authorization", fakeToken)) // 인증 토큰 제공
                .andExpect(status().isCreated())
                .andDo(document(
                    "chat-rooms/rooms",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())))
                .andReturn();
        }

//        @Test
//        @DisplayName("채팅방 만들기 테스트 실패")
//        void 채팅방_만들기_실패() throws Exception {
//            // given
//            // 가짜 토큰
//            String fakeToken = "FAKE_TOKEN";
//            CreateRoomReq createRoomReq = new CreateRoomReq(chatRoom1.getChatRoomName());  // 이미 존재하는 채팅방 이름 사용
//
//            when(versionConfig.getVersion()).thenReturn("v1");
//            when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);
//
//            MvcResult result = mockMvc.perform(
//                    post("/api/"+versionConfig.getVersion()+"/chat-rooms/rooms", createRoomReq)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(createRoomReq))
//                        .header("Authorization", fakeToken)) // 인증 토큰 제공
//                .andExpect(status().isBadRequest())
//                .andDo(document(
//                    "chat-rooms/rooms",
//                    preprocessRequest(prettyPrint()),
//                    preprocessResponse(prettyPrint())))
//                .andReturn();
//        }


    }

    @Nested
    @DisplayName("채팅방 리스트 받기 테스트")
    class 채팅방_리스트_받기 {

        @Test
        @DisplayName("채팅방 리스트 받기  : 성공")
        void 채팅방_리스트_받기_성공() throws Exception {
            // given
            // 가짜 토큰
            String fakeToken = "FAKE_TOKEN";

            when(chatMessageService.getChatRoomList()).thenReturn(
                Arrays.asList(chatRoom1, chatRoom2));
            when(versionConfig.getVersion()).thenReturn("v1");
            when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);

            MvcResult result = mockMvc.perform(
                    get("/api/" + versionConfig.getVersion() + "/chat-rooms/rooms")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", fakeToken)) // 인증 토큰 제공
                .andExpect(status().isOk())
                .andDo(document(
                    "chat-rooms/rooms",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())))
                .andReturn();
        }
    }

    @Nested
    @DisplayName("채팅방 삭제 테스트")
    class 채팅방_삭제 {

        @Test
        @DisplayName("채팅방 삭제 테스트  : 성공")
        void 채팅방_삭제_테스트() throws Exception {
            // given
            // 가짜 토큰
            String fakeToken = "FAKE_TOKEN";

            when(chatMessageService.deleteChatRoom(chatRoom1.getChatRoomName())).thenReturn(
                chatRoom1);
            when(versionConfig.getVersion()).thenReturn("v1");
            when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);

            MvcResult result = mockMvc.perform(
                    delete("/api/" + versionConfig.getVersion() + "/chat-rooms/rooms/{chatRoomName}",
                        chatRoom1.getChatRoomName())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(chatRoom1))
                        .header("Authorization", fakeToken)) // 인증 토큰 제공
                .andExpect(status().isOk())
                .andDo(document(
                    "chat-rooms/rooms/{chatRoomName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())))
                .andReturn();

        }
    }

    @Nested
    @DisplayName("채팅방 이름 수정 테스트")
    class 채팅방_이름_수정_성공 {

        @Test
        @DisplayName("채팅방 이름 수정 : 성공")
        void 채팅방_삭제_테스트() throws Exception {
            // given
            // 가짜 토큰
            String fakeToken = "FAKE_TOKEN";
            String NEW_ROOM_NAME = "춘천";

            ChatRoomEntity chatRoomNewNamedEntity = ChatRoomEntity.builder()
                .chatRoomId(chatRoom1.getChatRoomId())
                .chatRoomName(NEW_ROOM_NAME)
                .build();

            when(chatMessageService.updateChatRoom(chatRoom1.getChatRoomName(),
                NEW_ROOM_NAME)).thenReturn(chatRoomNewNamedEntity);
            when(versionConfig.getVersion()).thenReturn("v1");
            when(jwtUtil.createToken(TEST_EMAIL)).thenReturn(fakeToken);

            Map<String, String> updateData = new HashMap<>();
            updateData.put("newChatRoomName", NEW_ROOM_NAME);

            MvcResult result = mockMvc.perform(
                    put("/api/" + versionConfig.getVersion() + "/chat-rooms/chatRooms/{chatRoomName}",
                        chatRoom1.getChatRoomName())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateData)) // 수정된 부분
                        .header("Authorization", fakeToken)) // 인증 토큰 제공
                .andExpect(status().isOk())
                .andDo(document(
                    "chat-rooms/rooms/{chatRoomName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())))
                .andReturn();

        }
    }

    @Nested
    @DisplayName("채팅방 권한 테스트")
    class 채팅방_권한_테스트_성공 {

        @Test
        @DisplayName("채팅방 권한 테스트 : 성공")
        void 채팅방_권한_테스트_성공() throws Exception {
            // given
            String fakeToken = "FAKE_TOKEN";
            ChatRoleRes expectedResponse = ChatRoleRes.builder()
                .role("ROLE_USER")
                .name("name")
                .build();

            when(versionConfig.getVersion()).thenReturn("v1");
            when(jwtUtil.createToken("testUser")).thenReturn(fakeToken);

            // when & then
            mockMvc.perform(
                    get("/api/" + versionConfig.getVersion() + "/chat-rooms/users/getRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", fakeToken)) // 인증 토큰 제공
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value(expectedResponse.role()))
                .andExpect(jsonPath("$.data.name").value(expectedResponse.name()))
                .andDo(document(
                    "chat-rooms_users_getRole",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())));
        }

    }
}

