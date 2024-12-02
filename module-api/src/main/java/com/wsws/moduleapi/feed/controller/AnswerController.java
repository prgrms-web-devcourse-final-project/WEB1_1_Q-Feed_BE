package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiResponse;
import com.wsws.moduleapplication.feed.dto.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.feed.service.AnswerService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @PostMapping
    public ResponseEntity<AnswerPostApiResponse> postAnswers(
            @RequestBody AnswerPostApiRequest answerPostApiRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String userId = userPrincipal.getId(); // 사용자 아이디를 가져온다.
        AnswerCreateServiceResponse serviceResponse = answerService.createAnswer(answerPostApiRequest.toServiceDto(userId)); // 답변 생성

        return ResponseEntity.status(201).body(new AnswerPostApiResponse(serviceResponse));
    }

    /**
     * 답변 수정
     */

    /**
     * 답변 삭제
     */
}
