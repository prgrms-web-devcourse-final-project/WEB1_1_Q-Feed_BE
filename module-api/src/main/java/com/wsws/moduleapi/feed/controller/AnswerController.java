package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.MessageResponse;
import com.wsws.moduleapi.feed.dto.answer.AnswerPatchApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.feed.service.AnswerService;
import com.wsws.modulesecurity.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            @RequestBody AnswerPostApiRequest answerPostApiRequest
            ,@AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String userId = userPrincipal.getId(); // 사용자 아이디를 가져온다.
//        String userId = "user_id1";
        AnswerCreateServiceResponse serviceResponse = answerService.createAnswer(answerPostApiRequest.toServiceDto(userId)); // 답변 생성

        return ResponseEntity.status(201).body(new AnswerPostApiResponse(serviceResponse));
    }

    /**
     * 답변 수정
     */
    @PatchMapping("/{answer-id}")
    public ResponseEntity<MessageResponse> patchAnswer(@PathVariable("answer-id") Long answerId, @RequestBody AnswerPatchApiRequest answerPatchApiRequest) {
        answerService.editAnswer(answerPatchApiRequest.toServiceDto(answerId));
        return ResponseEntity.ok(new MessageResponse("답변이 수정되었습니다."));
    }

    /**
     * 답변 삭제
     */
    @DeleteMapping("/{answer-id}")
    public ResponseEntity<MessageResponse> deleteAnswer(@PathVariable("answer-id") Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok(new MessageResponse("답변이 삭제되었습니다."));
    }

    /**
     * 답변 좋아요 추가
     */
    @PostMapping("/{answer-id}/likes")
    public ResponseEntity<MessageResponse> likeToAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("answer-id") Long answerId) {

        String userId = userPrincipal.getId(); // 좋아요 누른 사용자 아이디 받아오기
//        String userId = "user_id1"; // 좋아요 누른 사용자 아이디 받아오기
        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER", answerId); // 도메인으로의 의존성을 피하기 위해 문자열로 넘겨줌
        answerService.addLikeToAnswer(request); // 해당 글에 좋아요 1 카운트

        return ResponseEntity.ok(new MessageResponse("좋아요가 추가되었습니다."));
    }


    /**
     * 답변 좋아요 취소
     */
    @PostMapping("/{answer-id}/cancel-likes")
    public ResponseEntity<MessageResponse> cancelLikeToAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("answer-id") Long answerId) {

        String userId = userPrincipal.getId(); // 좋아요 누른 사용자 아이디 받아오기
//        String userId = "user_id1"; // 좋아요 누른 사용자 아이디 받아오기

        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER", answerId); // 도메인으로의 의존성을 피하기 위해 문자열로 넘겨줌

        answerService.cancelLikeToAnswer(request); // 해당 글에 좋아요 1 마이너스

        return ResponseEntity.ok(new MessageResponse("좋아요가 취소되었습니다."));
    }
}