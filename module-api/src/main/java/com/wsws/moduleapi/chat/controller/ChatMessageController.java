package com.wsws.moduleapi.chat.controller;

import com.wsws.moduleapi.auth.dto.AuthResponse;
import com.wsws.moduleapi.chat.dto.ChatMessageApiResponse;
import com.wsws.moduleapi.chat.dto.ChatResponse;
import com.wsws.moduleapplication.chat.dto.ChatMessageRequest;
import com.wsws.moduleapplication.chat.dto.ChatMessageServiceResponse;
import com.wsws.moduleapplication.chat.service.ChatMessageService;
import com.wsws.moduleinfra.repo.chat.JpaChatMessageRepository;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final JpaChatMessageRepository jpaChatMessageRepository;

    @PostMapping("/{chatRoomId}/send")
    @Operation(summary = "메세지 전송", description = "특정 채팅방에 메세지를 전송합니다.")
    public ResponseEntity<ChatResponse> sendMessage(
            @Parameter(description = "메세지를 전송할 채팅방 ID") @PathVariable Long chatRoomId,
            @RequestBody ChatMessageRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        String userId = userPrincipal.getId();
        chatMessageService.sendMessage(chatRoomId,userId,request);
        return ResponseEntity.status(201).body(new ChatResponse("메세지가 전송되었습니다."));
    }

    // 채팅방의 메세지 조회 API
    @GetMapping("/{chatRoomId}/messages")
    @Operation(summary = "메세지 조회", description = "특정 채팅방의 모든 메세지를 조회합니다.")
    public ResponseEntity<List<ChatMessageApiResponse>> getMessages(
            @Parameter(description = "메세지를 조회할 채팅방 ID") @PathVariable Long chatRoomId,
            @Parameter(description = "커서로 사용할 마지막 팔로워의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String cursor,
            @Parameter(description = "페이지 크기", example = "100") @RequestParam(defaultValue = "300") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        String userId = userPrincipal.getId();

        LocalDateTime parsedCursor;
        try {
            parsedCursor = cursor != null ? LocalDateTime.parse(cursor) : LocalDateTime.now();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid cursor format. Please use the correct ISO format (e.g. 2024-01-01T00:00:00)");
        }

        List<ChatMessageServiceResponse> messages = chatMessageService.getChatMessages(chatRoomId,userId,parsedCursor, size);

        List<ChatMessageApiResponse> apiResponses = messages.stream()
                .map(ChatMessageApiResponse::new)  // 생성자에서 변환
                .toList();

        return ResponseEntity.ok(apiResponses);
    }

    @PutMapping("/{chatRoomId}/markasread")
    @Operation(summary = "메세지 읽음처리", description = "특정 채팅방의 읽지않은 모들 메세지를 읽음처리합니다.")
    public ResponseEntity<ChatResponse> markMessageAsRead(@Parameter(description = "메세지를 읽음처리할 채팅방 ID") @PathVariable Long chatRoomId) {
        chatMessageService.markAllMessagesAsRead(chatRoomId);
        return ResponseEntity.status(200).body(new ChatResponse("메세지가 읽음 처리 되었습니다."));
    }


}
