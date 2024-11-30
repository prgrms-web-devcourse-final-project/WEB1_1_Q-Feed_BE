package com.wsws.moduleexternalapi.feed.client;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RedisVectorClient {
    private final VectorStore vectorStore;

    /**
     * 벡터 데이터베이스에 텍스트 하나 저장
     */
    public void store(String text) {
        vectorStore.add(List.of(new Document(text)));
    }

    /**
     * 벡터 데이터베이스에 텍스트 여러 개 저장
     */
    public void store(List<String> texts) {
        vectorStore.add(texts.stream().map(Document::new).collect(Collectors.toList()));
    }

    /**
     * 비슷한 텍스트가 있는지
     */
    public boolean isSimilarTextExist(String text) {
        // 유사도가 0.8이상인 텍스트를 하나만 검색
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.query(text)
                        .withSimilarityThreshold(0.8)
                        .withTopK(1)
        );

        return !documents.isEmpty();
    }
}
