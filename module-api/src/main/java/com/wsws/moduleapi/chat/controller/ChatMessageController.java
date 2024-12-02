package com.wsws.moduleapi.chat.controller;

import com.wsws.moduleapi.chat.dto.ChatMessageApiResponse;
import com.wsws.moduleapplication.chat.dto.ChatMessageRequest;
import com.wsws.moduleapplication.chat.dto.ChatMessageServiceResponse;
import com.wsws.moduleapplication.chat.service.ChatMessageService;
import com.wsws.moduleinfra.repo.chat.JpaChatMessageRepository;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;
    private final JpaChatMessageRepository jpaChatMessageRepository;

    @PostMapping("/{chatRoomId}/send")
    public ResponseEntity<String> sendMessage( @PathVariable Long chatRoomId, @RequestBody ChatMessageRequest request,
                                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        // 메시지 전송 처리
        String userId = userPrincipal.getId();
        chatMessageService.sendMessage(chatRoomId,userId,request);
        return ResponseEntity.status(201).body("메세지가 전송되었습니다.");
    }

    // 채팅방의 메세지 조회 API
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageApiResponse>> getMessages(
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        //Pageable pageable = PageRequest.of(page, size);
        List<ChatMessageServiceResponse> messages = chatMessageService.getChatMessages(chatRoomId, page, size);

        List<ChatMessageApiResponse> apiResponses = messages.stream()
                .map(ChatMessageApiResponse::new)  // 생성자에서 변환
                .toList();

        return ResponseEntity.ok(apiResponses);
    }

    @PutMapping("/{chatRoomId}/markasread")
    public ResponseEntity<String> markMessageAsRead(@PathVariable Long chatRoomId) {
        chatMessageService.markAllMessagesAsRead(chatRoomId);
        return ResponseEntity.status(200).body("메세지가 읽음 처리 되었습니다.");
    }


}
