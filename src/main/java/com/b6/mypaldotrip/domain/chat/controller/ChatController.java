package com.b6.mypaldotrip.domain.chat.controller;

import com.b6.mypaldotrip.domain.chat.controller.dto.request.CreateRoomReq;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoleRes;
import com.b6.mypaldotrip.domain.chat.controller.dto.response.ChatRoomSaveRes;
import com.b6.mypaldotrip.domain.chat.service.ChatMessageService;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatMessage;
import com.b6.mypaldotrip.domain.chat.store.entity.ChatRoomEntity;
import com.b6.mypaldotrip.domain.comment.exception.CommentErrorCode;
import com.b6.mypaldotrip.domain.user.store.entity.UserRole;
import com.b6.mypaldotrip.global.common.GlobalResultCode;
import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.b6.mypaldotrip.global.response.RestResponse;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/${mpt.version}")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final VersionConfig versionConfig;

    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<List<ChatMessage>> findAllMessages(@PathVariable String chatRoomId) {
        return chatMessageService.findAllMessagesByChatRoomId(chatRoomId);
    }

    @MessageMapping("/chat/rooms")
    @SendTo("/topic/rooms")
    public ChatRoomSaveRes createChatRoom(@RequestBody CreateRoomReq req) {
        return chatMessageService.createARoom(req.chatRoomName());
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomEntity>> getChatRooms() {
        return ResponseEntity.ok(chatMessageService.getChatRoomList());
    }

    @MessageMapping("/chatting/{chatRoomId}")
    public void chatRoomToUsers(
            @DestinationVariable String chatRoomId, @Payload ChatMessage chatMessage) {

        ChatMessage chatRoom = chatMessageService.findByRoomIdAndSave(chatRoomId, chatMessage);

        messagingTemplate.convertAndSend("/topic/public/", chatMessage);
    }

    @DeleteMapping("/chat/{chatRoomName}")
    public ResponseEntity<RestResponse<ChatRoomEntity>> deleteChatRoom(
            @PathVariable String chatRoomName) {

        ChatRoomEntity chatRoomEntity = chatMessageService.deleteChatRoom(chatRoomName);

        return RestResponse.success(
                        chatRoomEntity, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @PutMapping("/chat/chatRoomName/{chatRoomName}/updateRoomName/{updateRoomName}")
    public ResponseEntity<RestResponse<ChatRoomEntity>> updateChatRoom(
            @PathVariable String chatRoomName, @PathVariable String updateRoomName) {

        ChatRoomEntity chatRoomEntity =
                chatMessageService.updateChatRoom(chatRoomName, updateRoomName);

        return RestResponse.success(
                        chatRoomEntity, GlobalResultCode.SUCCESS, versionConfig.getVersion())
                .toResponseEntity();
    }

    @GetMapping("/chat-page")
    public String chatPage() {
        return "index";
    }

    @GetMapping("/users/getRole")
    public ResponseEntity<ChatRoleRes> getRoleForChatRoomCRUD(
        @AuthenticationPrincipal UserDetailsImpl currentUser) {

        // UserRole 확인
        ChatRoleRes res = ChatRoleRes.builder()
            .role(currentUser.getUserEntity().getUserRole().name())
            .build();
        System.out.println("res.role() = " + res.role());

        // ROLE_ADMIN인지 확인
        if (currentUser.getUserEntity().getUserRole() == UserRole.ROLE_ADMIN) {
            // ROLE_ADMIN이면, "ROLE_ADMIN"을 반환
            return ResponseEntity.ok(res);
        } else {
            // ROLE_ADMIN이 아니면, "Not ROLE_ADMIN"을 반환
            throw new GlobalException(CommentErrorCode.USER_NOT_AUTHORIZED);
        }
    }
}
