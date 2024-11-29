package com.wsws.moduleapplication.dto.chat;

import com.wsws.moduledomain.chat.MessageType;
import org.springframework.web.multipart.MultipartFile;

public record ChatMessageRequest(String content, MessageType type, MultipartFile file) {
}
