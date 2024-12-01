package com.wsws.moduleexternalapi.feed.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsws.moduledomain.feed.question.ai.QuestionGenerateClient;
import com.wsws.moduleexternalapi.feed.util.TokenCalculateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class QuestionGenerateClientImpl implements QuestionGenerateClient {
    private final ChatModel chatModel;
    private final ChatOptions chatOptions;

    // 시스템 프롬프트
    private static final String SYSTEM_PROMPT = """ 
            You are an expert assistant that creates engaging and relevant content for the MZ generation in their 20s and 30s in Korea. Your task is to provide natural and appealing responses.
            """;

    // 유저 프롬프트
    private static final String USER_PROMPT = """ 
            Your task is to create one engaging and relevant question for each of the {categoriesSize} categories: {categories}
            The target audience is the MZ generation in their 20s and 30s in Korea. 
            Provide the questions in Korean and ensure the response is a 'pure JSON object without any Markdown formatting(do not include ```json)'. Ensure the questions are natural and appealing.
            """;

    //  제외시킬 질문들에 대한 프롬프트
    private static final String QUESTION_BLACKLIST_PROMPT = """
            Generate a "completely" different question from the following content.
            {questionBlackList}
            Ensure that the new question has no semantic, structural, or thematic similarity to the above questions. Avoid rephrasing or minor changes.
            """;


    /**
     * 생성해야하는 카테고리 목록들을 받아 프롬프트를 작성해 질문 생성
     */
    public Map<String, String> createQuestions(List<String> categories, Map<String, Set<String>> questionBlackListMap) {
        log.info("질문 생성 시도");
        log.info("질문 블랙리스트: {}", questionBlackListMap);
        Prompt prompt = createPrompt(categories, questionBlackListMap);
        ChatResponse response = chatModel.call(prompt);

        calculateTokens(response); // 사용된 토큰 계산하기

        String content = response.getResult().getOutput().getContent(); // json 형태의 질문들
        log.info("질문 생성 완료: {}", content);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode questionsNode = objectMapper.readTree(content); // 문자열 형태의 Json을 JsonNode로 파싱
            Map<String, String> questionMap = new HashMap<>();
            categories.forEach(category -> questionMap.put(category, questionsNode.get(category).asText())); // 뽑아낸 질문들을 Map에 저장
            return questionMap;
        } catch (JsonProcessingException e) {
            return Map.of(); // Json Parsing 오류시 빈 Map 반환
        }
    }

    /**
     * 누적 입력 토큰
     */
    @Override
    public Long getPromptTokens() {
        return TokenCalculateUtil.getPromptToken();
    }

    /**
     * 누적 출력 토큰
     */
    @Override
    public Long getGenerationTokens() {
        return TokenCalculateUtil.getGenerationToken();
    }

    /**
     * 누적 총합 토큰
     */
    @Override
    public Long getTotalTokens() {
        return TokenCalculateUtil.getTotalToken();
    }

    /**
     * 토큰 누적 시키기
     */
    private void calculateTokens(ChatResponse response) {
        TokenCalculateUtil.addPromptToken(response.getMetadata().getUsage().getPromptTokens()); // 사용된 입력 토큰 계산
        TokenCalculateUtil.addGenerationToken(response.getMetadata().getUsage().getGenerationTokens()); // 사용된 출력 토큰 계산
        TokenCalculateUtil.addTotalToken(response.getMetadata().getUsage().getTotalTokens()); // 사용된 누적 토큰 계산
    }


    /**
     * 프롬프트 생성
     */
    private Prompt createPrompt(List<String> categories, Map<String, Set<String>> questionBlackList) {

        List<Message> promptMessageList = new ArrayList<>();

        // 시스템 프롬프트 생성
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(SYSTEM_PROMPT);
        Message systemMessage = systemPromptTemplate.createMessage();
        promptMessageList.add(systemMessage);

        // 사용자 프롬프트 생성
        PromptTemplate userPromptTemplate = new PromptTemplate(USER_PROMPT);
        Message userMessage = userPromptTemplate.createMessage(Map.of("categoriesSize", categories.size(), "categories", categories.toString()));
        promptMessageList.add(userMessage);

        // 질문 블랙리스트 프롬프트 생성
        if(!questionBlackList.isEmpty()) {
            PromptTemplate questionBlackListPromptTemplate = new PromptTemplate(QUESTION_BLACKLIST_PROMPT);
            Message questionBlackListMessage = questionBlackListPromptTemplate.createMessage(Map.of("questionBlackList", questionBlackList.toString()));
            promptMessageList.add(questionBlackListMessage);
        }

        return new Prompt(promptMessageList, chatOptions);
    }
}
