package com.wsws.moduleexternalapi.feed.client;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class QuestionGenerateClient {
    private final ChatModel chatModel;
    private final ChatOptions chatOptions;

    private static final String SYSTEM_PROMPT = """ 
            You are an expert assistant that creates engaging and relevant content for the MZ generation in their 20s and 30s in Korea. Your task is to provide natural and appealing responses.
            """;
    private static final String USER_PROMPT = """ 
            Your task is to create one engaging and relevant question for each of the {categoriesSize} categories: {categories}
            The target audience is the MZ generation in their 20s and 30s in Korea. 
            Provide the questions in Korean and ensure the response is a 'pure JSON object without any Markdown formatting(do not include ```json)'. Ensure the questions are natural and appealing.
            """;

    /**
     * 생성해야하는 카테고리 목록들을 받아 프롬프트를 작성해 질문 생성
     */
    public Map<String, String> createQuestions(List<String> categories) {
        Prompt prompt = createPrompt(categories);
        System.out.println(prompt);
        ChatResponse response = chatModel.call(prompt);
        System.out.println(response);
        String content = response.getResult().getOutput().getContent();
        System.out.println(content);
        return null;
    }


    /**
     *
     * @param categories
     * @return
     */
    private Prompt createPrompt(List<String> categories) {

        // 시스템 프롬프트 생성
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        Message systemMessage = systemPromptTemplate.createMessage();

        // 사용자 프롬프트 생성
        PromptTemplate userPromptTemplate = new PromptTemplate(USER_PROMPT);
        Message userMessage = userPromptTemplate.createMessage(Map.of("categoriesSize", categories.size(), "categories", categories.toString()));

        return new Prompt(List.of(systemMessage, userMessage), chatOptions);
    }
}
