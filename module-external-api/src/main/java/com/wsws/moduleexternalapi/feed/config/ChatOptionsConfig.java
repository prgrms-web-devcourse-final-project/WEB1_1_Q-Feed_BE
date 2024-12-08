//package com.wsws.moduleexternalapi.feed.config;
//
//import com.wsws.moduledomain.category.vo.CategoryName;
//import org.springframework.ai.chat.prompt.ChatOptions;
//import org.springframework.ai.openai.OpenAiChatOptions;
//import org.springframework.ai.openai.api.ResponseFormat;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//import java.util.stream.Collectors;
//
//@Configuration
//public class ChatOptionsConfig {
//
//    /**
//     * OpenAI ChatOptions
//     * OpenAI의 장점 - 답변 형식 지정 가능
//     */
//    @Bean
//    public ChatOptions chatOptions() {
//
//        // 동적으로 CategoryName에서 JSON Schema 생성
//        String properties = Arrays.stream(CategoryName.values())
//                .map(category -> String.format("    \"%s\": {\n      \"type\": \"string\"\n    }", category.name()))
//                .collect(Collectors.joining(",\n"));
//
//        String required = Arrays.stream(CategoryName.values())
//                .map(CategoryName::name)
//                .map(name -> "\"" + name + "\"")
//                .collect(Collectors.joining(", "));
//
//        String jsonSchema = String.format("""
//                {
//                  "$schema": "https://json-schema.org/draft/2020-12/schema",
//                  "type": "object",
//                  "properties": {
//                %s
//                  },
//                  "required": [
//                    %s
//                  ],
//                  "additionalProperties": false
//                }
//                """, properties, required);
//
//        return OpenAiChatOptions.builder()
//                .withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
//                .build();
//    }
//}
