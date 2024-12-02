package com.wsws.moduleinfra.repo.feed;

import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import com.wsws.moduledomain.feed.question.vo.QuestionStatus;
import com.wsws.moduleinfra.entity.feed.QuestionEntity;
import com.wsws.moduleinfra.repo.feed.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final QuestionJpaRepository questionJpaRepository;

    @Override
    public List<Question> findByQuestionStatus(QuestionStatus questionStatus) {
        // QuestionEntity 리스트를 가져옴
        List<QuestionEntity> entities = questionJpaRepository.findByQuestionStatus(questionStatus);

        return entities.stream()
                .map(QuestionMapper::toDomain) // toDomain 메서드로 변환
                .toList(); // 최종적으로 리스트 반환
    }

    @Override
    public Question save(Question question) {
        QuestionEntity questionEntity = questionJpaRepository.save(QuestionMapper.toEntity(question));
        return QuestionMapper.toDomain(questionEntity);
    }

    @Override
    public Optional<Question> findByCategoryId(Long id) {
        return questionJpaRepository.findById(id)
                .map(QuestionMapper::toDomain); // 값이 있으면 변환하고, 없으면 빈 Optional 반환
    }
}
