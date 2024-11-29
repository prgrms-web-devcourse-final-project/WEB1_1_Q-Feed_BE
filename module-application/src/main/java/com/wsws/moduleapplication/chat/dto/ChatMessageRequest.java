package com.wsws.moduleapplication.chat.dto;

import com.wsws.moduledomain.chat.MessageType;
import org.springframework.web.multipart.MultipartFile;

public record ChatMessageRequest(String content, MessageType type, MultipartFile file) {
}
