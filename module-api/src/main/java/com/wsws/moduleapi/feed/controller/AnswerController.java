package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.MessageResponse;
import com.wsws.moduleapi.feed.dto.answer.AnswerPatchApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer.AnswerPostApiResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.feed.service.AnswerService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/answers")
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class AnswerController {

    private final AnswerService answerService;

    /**
     * 답변 목록 조회
     */

    /**
     * 답변 상세 조회
     */
    @Operation(summary = "답변 상세 조회", description = "답변의 상세목록을 조회합니다.")
    @GetMapping("/{answer-id}")
    public ResponseEntity<?> getAnswers(
            @Parameter(description = "답변 상세를 조회할 답변 ID") @PathVariable("answer-id") Long answerId) {
        answerService.findAnswerByAnswerId(answerId);
        return null;
    }

    /**
     * 답변 생성
     */
    @PostMapping
    @Operation(summary = "답변 생성", description = "현재 인증된 사용자로 답변을 생성합니다.")
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
    @Operation(summary = "답변 수정", description = "답변을 수정합니다.")
    public ResponseEntity<MessageResponse> patchAnswer(
            @Parameter(description = "수정할 답변 ID") @PathVariable("answer-id") Long answerId,
            @RequestBody AnswerPatchApiRequest answerPatchApiRequest) {
        answerService.editAnswer(answerPatchApiRequest.toServiceDto(answerId));
        return ResponseEntity.ok(new MessageResponse("답변이 수정되었습니다."));
    }

    /**
     * 답변 삭제
     */
    @DeleteMapping("/{answer-id}")
    @Operation(summary = "답변 삭제", description = "답변을 삭제합니다.")
    public ResponseEntity<MessageResponse> deleteAnswer(
            @Parameter(description = "삭제할 답변 ID") @PathVariable("answer-id") Long answerId) {
        answerService.deleteAnswer(answerId);
        return ResponseEntity.ok(new MessageResponse("답변이 삭제되었습니다."));
    }

    /**
     * 답변 좋아요 추가
     */
    @PostMapping("/{answer-id}/likes")
    @Operation(summary = "답변 좋아요 추가", description = "현재 인증된 사용자로 답변 좋아요를 추가합니다.")
    public ResponseEntity<MessageResponse> likeToAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 추가할 답변 ID") @PathVariable("answer-id") Long answerId) {

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
    @Operation(summary = "답변 좋아요 취소", description = "현재 인증된 사용자로 답변 좋아요를 취소합니다.")
    public ResponseEntity<MessageResponse> cancelLikeToAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 취소할 답변 ID") @PathVariable("answer-id") Long answerId) {

        String userId = userPrincipal.getId(); // 좋아요 누른 사용자 아이디 받아오기
//        String userId = "user_id1"; // 좋아요 누른 사용자 아이디 받아오기

        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER", answerId); // 도메인으로의 의존성을 피하기 위해 문자열로 넘겨줌

        answerService.cancelLikeToAnswer(request); // 해당 글에 좋아요 1 마이너스

        return ResponseEntity.ok(new MessageResponse("좋아요가 취소되었습니다."));
    }
}