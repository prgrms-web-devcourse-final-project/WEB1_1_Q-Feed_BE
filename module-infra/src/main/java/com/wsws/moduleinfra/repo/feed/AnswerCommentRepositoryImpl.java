package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduleinfra.entity.feed.mapper.AnswerCommentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerCommentRepositoryImpl implements AnswerCommentRepository {

    private final JpaAnswerCommentRepository jpaAnswerCommentRepository;

    @Override
    public Optional<AnswerComment> findById(Long id) {
        return jpaAnswerCommentRepository.findById(id)
                .map(AnswerCommentEntityMapper::toDomain);
    }
}
