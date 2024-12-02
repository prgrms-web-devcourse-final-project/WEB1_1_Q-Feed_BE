package com.wsws.moduledomain.chat.vo;

import java.time.LocalDateTime;
import java.util.List;

public record ChatRoomInfraDto(
        Long id,
        String userId,
        String userId2,
        LocalDateTime createdAt
) {
}
