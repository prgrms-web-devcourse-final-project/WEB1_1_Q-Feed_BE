package com.wsws.moduledomain.feed.question.ai;

import java.util.List;

public interface VectorClient {
    /**
     * 벡터 데이터베이스에 텍스트 하나 저장
     */
    void store(String text);

    /**
     * 벡터 데이터베이스에 텍스트 여러 개 저장
     */
    void store(List<String> texts);

    /**
     * 비슷한 텍스트를 찾아 반환
     */
    List<String> findSimilarText(String text);
}
