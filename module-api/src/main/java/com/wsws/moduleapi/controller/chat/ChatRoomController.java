package com.wsws.moduleapi.controller.chat;

import com.wsws.moduleapi.dto.chat.ChatRoomApiResponse;
import com.wsws.moduleapplication.dto.chat.ChatRoomServiceRequest;
import com.wsws.moduleapplication.dto.chat.ChatRoomServiceResponse;
import com.wsws.moduleapplication.service.chat.ChatRoomService;
import com.wsws.moduledomain.chat.ChatRoom;
import com.wsws.moduleinfra.repo.chat.JpaChatRoomRepository;
import com.wsws.moduleinfra.repo.chat.dto.ChatRoomInfraDTO;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JpaChatRoomRepository chatRoomRepository;

    //채팅방 생성
    @PostMapping
    public ResponseEntity<String> createChatRoom(@RequestBody ChatRoomServiceRequest req) {
        chatRoomService.createChatRoom(req);
        return ResponseEntity.status(201).body("채팅방 생성이 완료되었습니다.");
    }

    //채팅방 삭제
    @DeleteMapping("/{chatId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long chatId) {
        chatRoomService.deleteChatRoom(chatId);
        return ResponseEntity.status(200).body("채팅방이 삭제되었습니다.");
    }

    //채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomServiceResponse>> getChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();

        List<ChatRoomServiceResponse> chatRooms = chatRoomService.getChatRooms(userId);

        // ChatRoomServiceResponse->ChatRoomApiResponse
        List<ChatRoomApiResponse> response = chatRooms.stream()
                .map(ChatRoomApiResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(chatRooms);
    }

    //채팅방 조회
    @GetMapping("/{userId2}")
    public ResponseEntity<ChatRoomApiResponse> getChatRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String userId2) {
        String userId = userPrincipal.getId();
        ChatRoomApiResponse response = new ChatRoomApiResponse(chatRoomService.getChatRoomWithOtherUser(userId,userId2));
        return ResponseEntity.ok(response);
    }

}
