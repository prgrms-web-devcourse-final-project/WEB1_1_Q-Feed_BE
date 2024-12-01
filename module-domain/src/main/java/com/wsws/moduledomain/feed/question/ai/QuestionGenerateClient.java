package com.wsws.moduledomain.feed.question.ai;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QuestionGenerateClient {
    /**
     * 생성해야하는 카테고리 목록들을 받아 프롬프트를 작성해 질문 생성
     */
    Map<String, String> createQuestions(List<String> categories, Map<String, Set<String>> questionBlackListMap);
}
