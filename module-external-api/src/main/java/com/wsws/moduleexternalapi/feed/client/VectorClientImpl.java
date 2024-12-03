package com.wsws.moduleexternalapi.feed.client;

import com.wsws.moduledomain.feed.question.ai.VectorClient;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VectorClientImpl implements VectorClient {
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
     * 비슷한 텍스트를 찾아 반환
     */
    public List<String> findSimilarText(String text) {
        // 유사도가 0.8이상인 텍스트를 모두 검색
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.query(text)
                        .withSimilarityThreshold(0.8)
                        .withTopK(10000) // 최댓값 10000
        );

        return documents.stream()
                .map(Document::getContent)
                .collect(Collectors.toList());
    }
}
