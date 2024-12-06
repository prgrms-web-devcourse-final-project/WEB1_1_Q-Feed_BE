package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerCommentEntityMapper;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    @Override
//    public Optional<Answer> findByIdWithComments(Long id) {
//        return jpaAnswerRepository.findByIdWithComments(id)
//                .map(answerEntity -> {
//                    List<AnswerComment> answerComments = answerEntity.getAnswerCommentEntities().stream()
//                            .map(AnswerCommentEntityMapper::toDomain)
//                            .toList(); // AnswerCommentEntity -> AnswerComment
//                    Answer answer = AnswerEntityMapper.toDomain(answerEntity);
//                    answerComments.forEach(answer::addComments);  // 답변 댓글 추가
//                    return answer;
//                });
//    }

    /**
     * 답변 저장
     */
    @Override
    @Transactional
    public Answer save(Answer answer) {
        QuestionEntity questionEntity = jpaQuestionRepository.findById(answer.getQuestionId().getValue())
                .orElse(null);
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
        Optional<AnswerEntity> answerEntity = jpaAnswerRepository.findById(answer.getAnswerId().getValue());
        answerEntity
                .ifPresent(entity -> entity.editQuestionEntity(answer.getContent(), answer.getVisibility(), answer.getUrl(), answer.getLikeCount()));

    }

    /**
     * 답변 삭제
     */
    @Override
    public void deleteById(Long id) {
        jpaAnswerRepository.deleteById(id);
    }
}