package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer.AnswerCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerEditServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceResponse;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.feed.exception.QuestionNotFoundException;
import com.wsws.moduleapplication.user.exception.ProfileImageProcessingException;
import com.wsws.moduleapplication.util.FileValidator;
import com.wsws.modulecommon.service.FileStorageService;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.question.Question;
import com.wsws.moduledomain.feed.question.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final FileStorageService fileStorageService;

    public AnswerFindServiceResponse findAnswerByAnswerId(Long answerId) {
        Answer answer = answerRepository.findByAnswerId(answerId).orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        return null;
    }

    /**
     * 답변 생성
     */
    @Transactional
    public AnswerCreateServiceResponse createAnswer(AnswerCreateServiceRequest request) {
        String url = processImage(request.image()); // 이미지 처리

        Question question = questionRepository.findById(request.questionId())
                .orElseThrow(() -> QuestionNotFoundException.EXCEPTION); // 의존관계가 있는 Question을 불러옴

        // 답변 도메인 생성
        Answer answer = Answer.create(
                null,
                request.content(),
                request.visibility(),
                url,
                0,
                request.questionId(),
                request.userId()
        );

        Answer saved = null; // 저장
        try {
            saved = answerRepository.save(answer, question);
        } catch (RuntimeException e) {
            throw QuestionNotFoundException.EXCEPTION;
        }

        return new AnswerCreateServiceResponse(saved.getAnswerId().getValue());
    }

    /**
     * 답변 수정
     */
    @Transactional
    public void editAnswer(AnswerEditServiceRequest request) {
        String url = processImage(request.image()); // 이미지 처리

        Answer answer = answerRepository.findByAnswerId(request.answerId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        answer.editAnswer(request.content(), request.visibility(), url);

        try {
            answerRepository.edit(answer);
        } catch (RuntimeException e) {
            throw AnswerNotFoundException.EXCEPTION;
        }
    }

    /**
     * 답변 삭제
     */
    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }



    /**
     * 이미지 처리
     */
    private String processImage(MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            try {
                FileValidator.validate(image, "image");
                return fileStorageService.saveFile(image);
            } catch (Exception e) {
                throw ProfileImageProcessingException.EXCEPTION;
            }
        }
        return null; // 이미지가 없는 경우
    }
}
