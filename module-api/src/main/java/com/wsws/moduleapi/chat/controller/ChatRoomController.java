package com.wsws.moduleapi.chat.controller;

import com.wsws.moduleapi.chat.dto.ChatResponse;
import com.wsws.moduleapi.chat.dto.ChatRoomApiResponse;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceRequest;
import com.wsws.moduleapplication.chat.dto.ChatRoomServiceResponse;
import com.wsws.moduleapplication.chat.service.ChatRoomService;
import com.wsws.moduleinfra.repo.chat.JpaChatRoomRepository;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JpaChatRoomRepository chatRoomRepository;

    //채팅방 생성
    @PostMapping
    @Operation(summary = "채팅방 생성", description = "특정 사용자와의 채팅방을 생성합니다.")
    public ResponseEntity<ChatResponse> createChatRoom(@RequestBody ChatRoomServiceRequest req) {
        chatRoomService.createChatRoom(req);
        return ResponseEntity.status(201).body(new ChatResponse("채팅방 생성이 완료되었습니다."));
    }

    //채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    @Operation(summary = "채팅방 삭제", description = "특정 사용자와의 채팅방을 삭제합니다.")
    public ResponseEntity<ChatResponse> deleteChatRoom(
            @Parameter(description = "삭제할 채팅방 ID") @PathVariable Long chatRoomId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();
        chatRoomService.deleteChatRoom(chatRoomId,userId);
        return ResponseEntity.status(200).body(new ChatResponse("채팅방이 삭제되었습니다."));
    }

    //채팅방 목록 조회
    @GetMapping
    @Operation(summary = "채팅방 목록 조회", description = "주어진 사용자 ID의 채팅방 목록을 조회합니다.")
    public ResponseEntity<List<ChatRoomApiResponse>> getChatRooms(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String userId = userPrincipal.getId();

        List<ChatRoomServiceResponse> chatRooms = chatRoomService.getChatRooms(userId);

        // ChatRoomServiceResponse->ChatRoomApiResponse
        List<ChatRoomApiResponse> response = chatRooms.stream()
                .map(ChatRoomApiResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    //채팅방 찾기
    @GetMapping("/{nickname}")
    @Operation(summary = "채팅방 조회", description = "사용자 닉네임을 입력해 채팅방을 찾습니다.")
    public ResponseEntity<ChatRoomApiResponse> getChatRoom(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "존재하는 채팅방을 찾기위한 사용자 닉네임") @PathVariable String nickname) {
        String userId = userPrincipal.getId();
        ChatRoomApiResponse response = new ChatRoomApiResponse(chatRoomService.getChatRoomWithOtherUser(userId,nickname));
        return ResponseEntity.ok(response);
    }

}
