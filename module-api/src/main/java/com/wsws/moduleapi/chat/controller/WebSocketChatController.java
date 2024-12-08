package com.wsws.moduleapi.chat.controller;

import com.wsws.moduleapplication.chat.dto.WebSocketChatRequest;
import com.wsws.moduleapplication.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class WebSocketChatController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void sendMessage(WebSocketChatRequest webSocketChatRequest)
    {
        System.out.println("서버에서 받은 메시지: " + webSocketChatRequest);
        chatMessageService.sendMessage(
                webSocketChatRequest.roomId(),
                webSocketChatRequest.senderId(),
                webSocketChatRequest.toChatMessageRequest()
        );
    }
}
