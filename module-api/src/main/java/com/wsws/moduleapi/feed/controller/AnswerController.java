package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapplication.feed.service.AnswerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/answers")
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 목록 조회
     */

    /**
     * 답변 상세 조회
     */
    @GetMapping("/{answer-id}")
    public ResponseEntity<?> getAnswers(@PathVariable("answer-id") Long answerId) {
        answerService.findAnswerByAnswerId(answerId);
        return null;
    }

    /**
     * 답변 생성
     */

    /**
     * 답변 수정
     */

    /**
     * 답변 삭제
     */
}
