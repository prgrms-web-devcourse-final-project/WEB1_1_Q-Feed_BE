package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.AnswerCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.AnswerFindServiceResponse;
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

        Answer saved = answerRepository.save(answer, question); // 저장
        return new AnswerCreateServiceResponse(saved.getAnswerId().getValue());
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
