package com.wsws.moduleinfra.repo.feed;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduledomain.feed.dto.AnswerCommentCountDTO;
import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerCommentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wsws.moduleinfra.entity.feed.QAnswerCommentEntity.answerCommentEntity;

@Repository
@RequiredArgsConstructor
public class AnswerCommentRepositoryImpl implements AnswerCommentRepository {

    private final JpaAnswerCommentRepository jpaAnswerCommentRepository;
    private final JpaAnswerRepository jpaAnswerRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AnswerComment> findById(Long id) {
        return jpaAnswerCommentRepository.findById(id)
                .map(AnswerCommentEntityMapper::toDomain);
    }

    @Override
    public List<AnswerComment> findParentCommentsByAnswerIdWithCursor(Long answerId, LocalDateTime commentCursor, int size) {

        return queryFactory
                .selectFrom(answerCommentEntity)
                .where(
                        answerCommentEntity.answerEntity.id.eq(answerId),
                        answerCommentEntity.parentCommentEntity.id.isNull(),
                        answerCommentEntity.createdAt.lt(commentCursor)
                ).orderBy(answerCommentEntity.createdAt.desc())
                .offset(0)
                .limit(size)
                .fetch().stream()
                .map(AnswerCommentEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<AnswerComment> findByAnswerIdWithCursor(Long answerId, LocalDateTime commentCursor, int size) {
        return queryFactory
                .selectFrom(answerCommentEntity)
                .where(
                        answerCommentEntity.answerEntity.id.eq(answerId),
                        answerCommentEntity.createdAt.lt(commentCursor)
                ).orderBy(answerCommentEntity.createdAt.desc())
                .offset(0)
                .limit(size)
                .fetch()
                .stream()
                .map(AnswerCommentEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<AnswerComment> findChildCommentsByParentsId(List<Long> parentIds) {
        return jpaAnswerCommentRepository.findChildCommentsByParentsId(parentIds).stream()
                .map(AnswerCommentEntityMapper::toDomain)
                .toList();
    }

    @Override
    public int countParentCommentByAnswerId(Long answerId) {
        return jpaAnswerCommentRepository.countParentCommentByAnswerId(answerId);
    }

    @Override
    public List<AnswerCommentCountDTO> countCommentsByAnswerIds(List<Long> answerIds) {

        return queryFactory
                .select(Projections.constructor(
                        AnswerCommentCountDTO.class,
                        answerCommentEntity.answerEntity.id,
                        answerCommentEntity.count()
                ))
                .from(answerCommentEntity)
                .groupBy(answerCommentEntity.answerEntity.id)
                .having(answerCommentEntity.answerEntity.id.in(answerIds))
                .fetch();
    }

    @Override
    public List<AnswerCommentCountDTO> countCommentsByAnswerCommentIds(List<Long> answerCommentIds) {
        return queryFactory
                .select(Projections.constructor(
                        AnswerCommentCountDTO.class,
                        answerCommentEntity.answerEntity.id,
                        answerCommentEntity.count()
                ))
                .from(answerCommentEntity)
                .groupBy(answerCommentEntity.id)
                .having(answerCommentEntity.id.in(answerCommentIds))
                .fetch();
    }

    @Override
    public AnswerComment save(AnswerComment answerComment) {
        AnswerCommentEntity answerCommentEntity = AnswerCommentEntityMapper.toEntity(answerComment);

        jpaAnswerRepository.findById(answerComment.getAnswerId().getValue())
                .ifPresent(answerCommentEntity::setAnswerEntity);

        // 부모 댓글이 있는 경우
        if (answerComment.getParentAnswerCommentId().getValue() != null) { // NPE 방지
            jpaAnswerCommentRepository.findById(answerComment.getParentAnswerCommentId().getValue())
                    .ifPresent(answerCommentEntity::setParentCommentEntity);
        }

        return AnswerCommentEntityMapper.toDomain(jpaAnswerCommentRepository.save(answerCommentEntity));
    }

    @Override
    public void edit(AnswerComment answerComment) {
        Optional<AnswerCommentEntity> answerCommentEntity = jpaAnswerCommentRepository.findById(answerComment.getAnswerCommentId().getValue());

        answerCommentEntity
                .ifPresent(entity -> entity.editAnswerCommentEntity(answerComment.getContent(), answerComment.getLikeCount()));
    }

    @Override
    public void deleteById(Long id) {
        jpaAnswerCommentRepository.deleteById(id);
    }
}
