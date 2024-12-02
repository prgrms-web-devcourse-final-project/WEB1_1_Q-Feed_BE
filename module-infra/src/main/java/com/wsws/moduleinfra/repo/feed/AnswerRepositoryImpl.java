package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final JpaAnswerRepository jpaAnswerRepository;
    private final JpaQuestionRepository jpaQuestionRepository;

    /**
     * 답변을 Id를 기준으로 찾기
     */
    @Override
    public Optional<Answer> findById(Long id) {

        return jpaAnswerRepository.findById(id)
                .map(AnswerEntityMapper::toDomain);
    }

    /**
     * 답변 저장
     */
    @Override
    @Transactional
    public Answer save(Answer answer, Question question) {
        QuestionEntity questionEntity = jpaQuestionRepository.findById(question.getQuestionId().getValue())
                .orElseThrow(RuntimeException::new);
        AnswerEntity answerEntity = AnswerEntityMapper.toEntity(answer);
        answerEntity.setQuestionEntity(questionEntity); // 연관관계 설정

        AnswerEntity savedEntity = jpaAnswerRepository.save(answerEntity);// Answer를 엔티티로 변환하여 저장
        return AnswerEntityMapper.toDomain(savedEntity);
    }

    /**
     * 답변 수정
     * 수정된 Answer 객체가 넘어온다.
     */
    @Override
    public void edit(Answer answer) {
        AnswerEntity answerEntity = jpaAnswerRepository.findById(answer.getAnswerId().getValue())
                .orElseThrow(RuntimeException::new);
        answerEntity.editEntity(answer.getContent(), answer.getVisibility(), answer.getUrl(), answerEntity.getReactionCount());
    }

    /**
     * 답변 삭제
     */
    @Override
    public void deleteById(Long id) {
        jpaAnswerRepository.deleteById(id);
    }
}