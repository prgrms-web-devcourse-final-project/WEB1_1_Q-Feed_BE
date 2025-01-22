package com.wsws.moduleinfra.repo.feed;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.wsws.moduledomain.feed.like.TargetType.ANSWER;
import static com.wsws.moduledomain.feed.question.vo.QuestionStatus.ACTIVATED;
import static com.wsws.moduleinfra.entity.feed.QAnswerCommentEntity.answerCommentEntity;
import static com.wsws.moduleinfra.entity.feed.QAnswerEntity.answerEntity;
import static com.wsws.moduleinfra.entity.feed.QLikeEntity.likeEntity;
import static com.wsws.moduleinfra.entity.feed.QQuestionEntity.questionEntity;
import static com.wsws.moduleinfra.socialnetworkcontext.follow.entity.QFollowEntity.followEntity;
import static com.wsws.moduleinfra.usercontext.user.entity.QUserEntity.userEntity;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final JpaAnswerRepository jpaAnswerRepository;
    private final JpaQuestionRepository jpaQuestionRepository;
    private final JPAQueryFactory queryFactory;

    /**
     * 답변을 Id를 기준으로 찾기
     */
    @Override
    public Optional<Answer> findById(Long id) {

        return jpaAnswerRepository.findById(id)
                .map(AnswerEntityMapper::toDomain);
    }

    @Override
    public List<Answer> findAllByCategoryIdWithCursor(LocalDateTime cursor, int size, Long categoryId) {
        List<AnswerEntity> answerEntities = queryFactory
                .selectFrom(answerEntity)
                .join(answerEntity.questionEntity, questionEntity)
                .where(
                        categoryIdEq(categoryId),
                        questionEntity.questionStatus.eq(ACTIVATED),
                        answerEntity.createdAt.lt(cursor)
                ).orderBy(answerEntity.createdAt.desc())
                .offset(0)
                .limit(size)
                .fetch();

        return answerEntities.stream()
                .map(AnswerEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<AnswerQuestionDTO> findAllByUserIdWithCursor(
            String userId, LocalDateTime cursor, int size, boolean isMine) {

        List<AnswerEntity> answerEntities = queryFactory
                .selectFrom(answerEntity)
                .join(answerEntity.questionEntity, questionEntity).fetchJoin()
                .where(
                        visibilityEqTrue(isMine),
                        answerEntity.userId.eq(userId),
                        answerEntity.createdAt.lt(cursor)
                ).orderBy(answerEntity.createdAt.desc())
                .offset(0)
                .limit(size)
                .fetch();

        return answerEntities.stream()
                .map(AnswerEntityMapper::toJoinDto)
                .toList();
    }

    @Override
    public Long countByUserId(String userId, boolean isMine) {
        return queryFactory
                .select(answerEntity.count())
                .from(answerEntity)
                .where(
                        visibilityEqTrue(isMine),
                        answerEntity.userId.eq(userId)
                ).fetchFirst();
    }

    @Override
    public Optional<Answer> findAnswerByUserIdAndQuestionId(String userId, Long questionId) {
        return jpaAnswerRepository.findAnswerByUserIdAndQuestionId(userId, questionId)
                .map(AnswerEntityMapper::toDomain);
    }

    @Override
    public List<Answer> findAnswersByLikeCountAndCategoryIdWithCursor(Long categoryId, int limit) {

        List<AnswerEntity> answerEntities = queryFactory
                .selectFrom(answerEntity)
                .join(answerEntity.questionEntity, questionEntity)
                .where(
                        categoryIdEq(categoryId),
                        questionEntity.questionStatus.eq(ACTIVATED)
                ).orderBy(answerEntity.likeCount.desc())
                .offset(0)
                .limit(limit)
                .fetch();

        return answerEntities.stream()
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

        jpaQuestionRepository.findById(answer.getQuestionId().getValue())
                .ifPresent(answerEntity::setQuestionEntity); // Quesiton 연관관계 설정

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

    private BooleanBuilder categoryIdEq(Long categoryId) {
        return nullSafeBuilder(() -> questionEntity.categoryId.eq(categoryId), categoryId);
    }

    private BooleanBuilder visibilityEqTrue(boolean isMine) {
        return nullSafeBuilder(() -> answerEntity.visibility.eq(true), null);
    }

    private <T> BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f, T value) {
        if (value instanceof String && !hasText((String) value)) {
            return new BooleanBuilder();
        }
        try {
            return new BooleanBuilder(f.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }
}