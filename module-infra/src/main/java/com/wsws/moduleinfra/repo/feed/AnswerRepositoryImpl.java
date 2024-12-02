package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import com.wsws.moduleinfra.repo.feed.mapper.AnswerMapper;
import com.wsws.moduleinfra.repo.feed.mapper.QuestionMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final AnswerJpaRepository answerJpaRepository;
    private final EntityManager em;

    /**
     * 답변을 Id를 기준으로 찾기
     */
    @Override
    public Optional<Answer> findByAnswerId(Long id) {

        return answerJpaRepository.findById(id)
                .map(AnswerMapper::toDomain);
    }

    /**
     * 답변 저장
     */
    @Override
    @Transactional
    public Answer save(Answer answer, Question question) {
        QuestionEntity questionEntity = em.find(QuestionEntity.class, question.getQuestionId().getValue());
        AnswerEntity answerEntity = AnswerMapper.toEntity(answer);
        answerEntity.setQuestionEntity(questionEntity); // 연관관계 설정

        AnswerEntity savedEntity = answerJpaRepository.save(answerEntity);// Answer를 엔티티로 변환하여 저장
        return AnswerMapper.toDomain(savedEntity);
    }
}
