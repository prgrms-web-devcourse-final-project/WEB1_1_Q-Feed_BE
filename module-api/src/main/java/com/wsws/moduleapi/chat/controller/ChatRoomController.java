package com.wsws.moduleapi.chat.controller;

import com.wsws.moduleapi.chat.dto.ChatRoomApiResponse;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceRequest;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;
import com.wsws.moduleapplication.chat.service.ChatRoomService;
import com.wsws.moduleinfra.repo.chat.JpaChatRoomRepository;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
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
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return ResponseEntity.status(200).body("채팅방이 삭제되었습니다.");
    }

    //채팅방 목록 조회
    @GetMapping
    public ResponseEntity<List<ChatRoomApiResponse>> getChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();

        List<ChatRoomServiceResponse> chatRooms = chatRoomService.getChatRooms(userId);

        // ChatRoomServiceResponse->ChatRoomApiResponse
        List<ChatRoomApiResponse> response = chatRooms.stream()
                .map(ChatRoomApiResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    //채팅방 조회
    @GetMapping("/{userId2}")
    public ResponseEntity<ChatRoomApiResponse> getChatRoom(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String userId2) {
        String userId = userPrincipal.getId();
        ChatRoomApiResponse response = new ChatRoomApiResponse(chatRoomService.getChatRoomWithOtherUser(userId,userId2));
        return ResponseEntity.ok(response);
    }

}
