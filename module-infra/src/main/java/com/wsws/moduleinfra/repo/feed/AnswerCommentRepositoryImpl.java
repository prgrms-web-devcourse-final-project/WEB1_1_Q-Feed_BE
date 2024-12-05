package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduleinfra.entity.feed.AnswerCommentEntity;
import com.wsws.moduleinfra.entity.feed.AnswerEntity;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerCommentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerCommentRepositoryImpl implements AnswerCommentRepository {

    private final JpaAnswerCommentRepository jpaAnswerCommentRepository;
    private final JpaAnswerRepository jpaAnswerRepository;

    @Override
    public Optional<AnswerComment> findById(Long id) {
        return jpaAnswerCommentRepository.findById(id)
                .map(AnswerCommentEntityMapper::toDomain);
    }

    @Override
    public AnswerComment save(AnswerComment answerComment) {
        AnswerCommentEntity answerCommentEntity = AnswerCommentEntityMapper.toEntity(answerComment);

        AnswerEntity answerEntity = jpaAnswerRepository.findById(answerComment.getAnswerId().getValue())
                .orElse(null); // 연관관계에 잇는 AnswerEntity 가져오기

        answerCommentEntity.setAnswerEntity(answerEntity); // 연관관계 설정

        if (answerComment.getParentAnswerCommentId().getValue() != null) { // NPE 방지
            AnswerCommentEntity parentCommentEntity =
                    jpaAnswerCommentRepository.findById(answerComment.getParentAnswerCommentId().getValue())
                            .orElse(null);
            answerCommentEntity.setParentCommentEntity(parentCommentEntity);
        }

        return AnswerCommentEntityMapper.toDomain(jpaAnswerCommentRepository.save(answerCommentEntity));
    }

    @Override
    public void edit(AnswerComment answerComment) {
        Optional<AnswerCommentEntity> answerCommentEntity = jpaAnswerCommentRepository.findById(answerComment.getAnswerCommentId().getValue());

        answerCommentEntity
                .ifPresent(entity -> entity.editAnswerCommentEntity(answerComment.getContent(), answerComment.getReactionCount()));
    }

    @Override
    public void deleteById(Long id) {
        jpaAnswerCommentRepository.deleteById(id);
    }
}
