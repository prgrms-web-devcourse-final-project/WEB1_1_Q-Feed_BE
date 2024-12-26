package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public Optional<Answer> findByIdWithLock(Long id) {
        return jpaAnswerRepository.findByIdWithLock(id)
                .map(AnswerEntityMapper::toDomain);
    }

    @Override
    public List<Answer> findAllByCategoryIdWithCursor(LocalDateTime cursor, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(0, size); // 가져올 데이터 갯수 설정
        return categoryId == null
                ?
                jpaAnswerRepository.findAllWithCursor(cursor, pageable).stream() // categoryId가 없다면 전체 조회
                .map(AnswerEntityMapper::toDomain)
                .toList()
                :jpaAnswerRepository.findAllByCategoryIdWithCursor(cursor, pageable, categoryId).stream() // categoryId가 있다면 해당 카테고리로 조회
                .map(AnswerEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<AnswerQuestionDTO> findAllByUserIdWithCursor(
            String userId, LocalDateTime cursor, int size, boolean isMine) {
        Pageable pageable = PageRequest.of(0, size); // 가져올 데이터 갯수 설정
        List<AnswerEntity> answerEntities = isMine
                ? jpaAnswerRepository.findAllByUserIdWithCursor(userId, cursor, pageable)
                : jpaAnswerRepository.findAllByUserIdAndVisibilityTrueWithCursor(userId, cursor, pageable);

        return answerEntities.stream()
                .map(AnswerEntityMapper::toJoinDto)
                .toList();
    }


    @Override
    public Long countByUserId(String userId, boolean isMine) {
        return isMine ? jpaAnswerRepository.countByUserId(userId) // 요청한 사용자의 질문이면 모든 Answer
                : jpaAnswerRepository.countByUserIdAndVisibilityTrue(userId); // 요청한 사용자의 질문이 아니면 visibility가 true인 Answer만
    }

    @Override
    public Optional<Answer> findAnswerByUserIdAndQuestionId(String userId, Long questionId) {
        return jpaAnswerRepository.findAnswerByUserIdAndQuestionId(userId, questionId)
                .map(AnswerEntityMapper::toDomain);
    }

    @Override
    public List<Answer> findAnswersByLikeCountAndCategoryIdWithCursor(Long categoryId, int limit) {
        Pageable pageable = PageRequest.of(0, limit); // 가져올 데이터 갯수 설정
        return categoryId == null
                ? jpaAnswerRepository.findAllOrderByLikeCountDescWithCursor(pageable).stream() // categoryId가 없다면 전체 조회
                .map(AnswerEntityMapper::toDomain)
                .toList()
                : jpaAnswerRepository.findAllByCategoryIdOrderByLikeCountDescWithCursor(categoryId, pageable).stream() // categoryId가 있다면 해당 categoryId로 조회
                .map(AnswerEntityMapper::toDomain)
                .toList();
    }

    /**
     * 답변 저장
     */
    @Override
    @Transactional
    public Answer save(Answer answer) {
        AnswerEntity answerEntity = AnswerEntityMapper.toEntity(answer);

        QuestionEntity questionEntity = jpaQuestionRepository.findById(answer.getQuestionId().getValue()).orElse(null);
        answerEntity.setQuestionEntity(questionEntity); // Quesiton 연관관계 설정

        AnswerEntity savedEntity = jpaAnswerRepository.save(answerEntity);// Answer를 엔티티로 변환하여 저장
        return AnswerEntityMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByUserIdAndQuestionId(String userId, Long questionId) {
        return jpaAnswerRepository.existsByUserIdAndQuestionId(userId, questionId);
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