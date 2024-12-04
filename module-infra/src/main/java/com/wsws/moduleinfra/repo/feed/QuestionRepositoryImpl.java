package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import com.wsws.moduleinfra.entity.feed.mapper.QuestionEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final JpaQuestionRepository jpaQuestionRepository;

    /**
     * QuestionStatus로 질문들 찾기
     */
    @Override
    public List<Question> findByQuestionStatus(QuestionStatus questionStatus) {
        // 내보낼때 도메인으로 변환
        return jpaQuestionRepository.findByQuestionStatus(questionStatus).stream()
                .map(QuestionEntityMapper::toDomain)
                .toList();
    }

    /**
     * Id로 질문 찾기
     * @param id
     * @return
     */
    @Override
    public Optional<Question> findById(Long id) {
        return jpaQuestionRepository.findById(id)
                .map(QuestionEntityMapper::toDomain);
    }

    /**
     * 질문 저장
     */
    @Override
    public Question save(Question question) {
        // 들어올 때 엔티티로 변환 . 내보낼때 도메인으로 변환
        return QuestionEntityMapper.toDomain(
                jpaQuestionRepository.save(
                        QuestionEntityMapper.toEntity(question)));
    }

    /**
     * 오늘의 질문을 카테고리 기준으로 찾기
     */
    @Override
    public Optional<Question> findDailyQuestionByCategoryId(Long categoryId) {
        // QuestionEntity를 가져옴
        Optional<QuestionEntity> dailyQuestionEntity = jpaQuestionRepository.findDailyQuestionByCategoryId(categoryId);

        // QuestionEntity를 Question으로 변환
        return dailyQuestionEntity.map(QuestionEntityMapper::toDomain);
    }


}