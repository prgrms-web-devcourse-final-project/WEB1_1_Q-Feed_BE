package com.wsws.moduleexternalapi.ai.config;

import com.wsws.moduleexternalapi.ai.client.AIQuestionClient;
import com.wsws.moduleexternalapi.ai.dto.AIQuestionResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatOptionsConfig {

    /**
     * OpenAI ChatOptions
     * OpenAI의 장점 - 답변형식 지정 가능
     */
    @Bean
    public ChatOptions chatOptions() {
        BeanOutputConverter<AIQuestionResponse> outputConverter = new BeanOutputConverter<>(AIQuestionResponse.class);
        String jsonSchema = outputConverter.getJsonSchema();

        return OpenAiChatOptions.builder().
                withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema)).
                build();

    }
}
