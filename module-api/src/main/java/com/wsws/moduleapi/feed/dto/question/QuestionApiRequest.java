package com.wsws.moduleapi.feed.dto.question;

import java.util.Map;

public record QuestionApiRequest(
        Map<String, String> questions
) {
}
