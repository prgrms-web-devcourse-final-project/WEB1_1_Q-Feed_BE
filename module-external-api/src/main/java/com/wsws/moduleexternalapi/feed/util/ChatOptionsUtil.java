package com.wsws.moduleexternalapi.feed.util;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;

import java.util.List;
import java.util.stream.Collectors;

public class ChatOptionsUtil {

    public static ChatOptions buildChatOptions(List<String> categories) {
        // 동적으로 categories 리스트에서 JSON Schema 생성
        String properties = categories.stream()
                .map(category -> String.format("    \"%s\": {\n      \"type\": \"string\"\n    }", category))
                .collect(Collectors.joining(",\n"));

        String required = categories.stream()
                .map(category -> "\"" + category + "\"")
                .collect(Collectors.joining(", "));

        String jsonSchema = String.format("""
            {
              "$schema": "https://json-schema.org/draft/2020-12/schema",
              "type": "object",
              "properties": {
            %s
              },
              "required": [
                %s
              ],
              "additionalProperties": false
            }
            """, properties, required);

        return OpenAiChatOptions.builder()
                .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
                .build();
    }

}
