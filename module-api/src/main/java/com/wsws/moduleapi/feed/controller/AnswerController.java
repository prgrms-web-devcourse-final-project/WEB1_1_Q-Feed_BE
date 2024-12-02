package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.answer.AnswerPatchApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.feed.service.AnswerService;
import com.wsws.moduleapplication.user.service.LikeService;
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
    private final LikeService likeService;

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
//            ,@AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
//        String userId = userPrincipal.getId(); // 사용자 아이디를 가져온다.
        String userId = "user_id1";
        AnswerCreateServiceResponse serviceResponse = answerService.createAnswer(answerPostApiRequest.toServiceDto(userId)); // 답변 생성

        return ResponseEntity.status(201).body(new AnswerPostApiResponse(serviceResponse));
    }

    /**
     * 답변 수정
     */
    @PatchMapping("/{answer-id}")
    public ResponseEntity<String> patchAnswer(@PathVariable("answer-id") Long answerId, @RequestBody AnswerPatchApiRequest answerPatchApiRequest) {
        answerService.editAnswer(answerPatchApiRequest.toServiceDto(answerId));
        return ResponseEntity.ok("답변이 수정되었습니다.");
    }

    /**
     * 답변 삭제
     */
    @DeleteMapping("/{answer-id}")
    public ResponseEntity<String> deleteAnswer(@PathVariable("answer-id") Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok("답변이 삭제되었습니다.");
    }

    /**
     * 답변 반응(좋아요)
     */
    @PostMapping("/{answer-id}/likes")
    public ResponseEntity<String> likeToAnswer(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("answer-id") Long answerId) {
//        String userId = userPrincipal.getId(); // 좋아요 누른 사용자 아이디 받아오기
        String userId = "user_id1"; // 좋아요 누른 사용자 아이디 받아오기
        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER", answerId); // 도메인으로의 의존성을 피하기 위해 문자열로 넘겨줌
        likeService.createLike(request); // Like 생성 및 저장
        answerService.addLikeToAnswer(request); // 해당 글에 좋아요 1 카운트
        return ResponseEntity.ok("좋아요가 추가되었습니다.");
    }
}