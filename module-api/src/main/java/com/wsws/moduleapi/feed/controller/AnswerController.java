package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.MessageResponse;
import com.wsws.moduleapi.feed.dto.answer.get.AnswerCountByUserGetApiResponse;
import com.wsws.moduleapi.feed.dto.answer.get.AnswerGetApiResponse;
import com.wsws.moduleapi.feed.dto.answer.get.AnswerListGetApiResponse;
import com.wsws.moduleapi.feed.dto.answer.post.AnswerPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer.post.AnswerPostApiResponse;
import com.wsws.moduleapi.feed.dto.answer.put_patch.AnswerPatchApiRequest;
import com.wsws.moduleapplication.feed.dto.answer.edit.AnswerCreateServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.read.*;
import com.wsws.moduleapplication.feed.service.AnswerReadService;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.feed.service.AnswerService;
import com.wsws.modulesecurity.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed/answers")
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class AnswerController {

    private final AnswerService answerService;
    private final AnswerReadService answerReadService;

    /**
     * 답변 목록 조회
     */
    @GetMapping
    @Operation(summary = "답변 목록 조회", description = "답변의 목록을 조회합니다. 댓글의 상세내용은 제공되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "작성자 정보를 찾을 수 없을 때", content = @Content)
    })
    public ResponseEntity<AnswerListGetApiResponse> getAnswerList(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "커서로 사용할 마지막 글의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String answerCursor,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    ) {

        String userId = userPrincipal.getId();
//        String reqUserId = "user_id1";
        LocalDateTime parsedCursor = answerCursor != null ? LocalDateTime.parse(answerCursor) : LocalDateTime.now();
        AnswerListFindServiceResponse answers = answerReadService.findAnswerListWithCursor(new AnswerFindServiceRequest(userId, null, parsedCursor, size));


        return ResponseEntity.ok(AnswerListGetApiResponse.toApiResponse(answers));
    }

    /**
     * 특정 사용자의 답변 목록 조회
     */
    @GetMapping("/{user-id}")
    public ResponseEntity<?> getAnswersByUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("user-id") String userId,
            @Parameter(description = "커서로 사용할 마지막 글의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String answerCursor,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {

        return null;
    }

    /**
     * 특정 사용자의 답변 총 갯수
     */
    @GetMapping("/{user-id}/count")
    public ResponseEntity<?> getAnswersByUserCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("user-id") String userId) {
        String reqUserId = userPrincipal.getId();
//        String reqUserId = "user_id2";

        AnswerCountByUserServiceResponse serviceResponse = answerReadService.countAnswersByUser(new AnswerCountByUserServiceRequest(reqUserId, userId));
        return ResponseEntity.ok(AnswerCountByUserGetApiResponse.toApiResponse(serviceResponse));
    }


    /**
     * 답변 상세 조회
     */
    @GetMapping("/{answer-id}")
    @Operation(summary = "답변 상세 조회", description = "답변의 상세목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 조회 성공"),
            @ApiResponse(responseCode = "404", description = "없는 답변 일 때", content = @Content),
            @ApiResponse(responseCode = "404", description = "작성자 정보를 찾을 수 없을 때", content = @Content)
    })
    public ResponseEntity<AnswerGetApiResponse> getAnswers(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "답변 상세를 조회할 답변 ID") @PathVariable("answer-id") Long answerId,
            @Parameter(description = "커서로 사용할 마지막 댓글의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String commentCursor,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "2") int size
    ) {
        String userId = userPrincipal.getId();
//        String reqUserId = "user_id1";

        LocalDateTime parsedCursor = commentCursor != null ? LocalDateTime.parse(commentCursor) : LocalDateTime.now();

        AnswerFindServiceResponse serviceResponse =
                answerReadService.findOneAnswerWithCursor(new AnswerFindServiceRequest(userId, answerId, parsedCursor, size));

        return ResponseEntity.ok(AnswerGetApiResponse.toApiResponse(serviceResponse));
    }

    /**
     * 답변 생성
     */
    @PostMapping
    @Operation(summary = "답변 생성", description = "현재 인증된 사용자로 답변을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "답변 작성 성공"),
            @ApiResponse(responseCode = "400", description = "필수입력값 - questionId, visibility가 누락됐을 때 "),
            @ApiResponse(responseCode = "404", description = "없는 질문일 때", content = @Content)
    })
    public ResponseEntity<AnswerPostApiResponse> postAnswers(
            @Valid @RequestBody AnswerPostApiRequest answerPostApiRequest
            , @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        String userId = userPrincipal.getId(); // 사용자 아이디를 가져온다.
//        String reqUserId = "user_id1";
        AnswerCreateServiceResponse serviceResponse = answerService.createAnswer(answerPostApiRequest.toServiceDto(userId)); // 답변 생성

        return ResponseEntity.status(201).body(new AnswerPostApiResponse(serviceResponse));
    }

    /**
     * 답변 수정
     */
    @PatchMapping("/{answer-id}")
    @Operation(summary = "답변 수정", description = "답변을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 수정 성공"),
            @ApiResponse(responseCode = "400", description = "필수 입력값 - visibility가 누락됐을 때"),
            @ApiResponse(responseCode = "404", description = "없는 답변 일 때", content = @Content)
    })
    public ResponseEntity<MessageResponse> patchAnswer(
            @Parameter(description = "수정할 답변 ID") @PathVariable("answer-id") Long answerId,
            @Valid @RequestBody AnswerPatchApiRequest answerPatchApiRequest) {
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 좋아요 성공"),
            @ApiResponse(responseCode = "400", description = "이미 좋아요를 누른적이 있는 글일 때", content = @Content),
            @ApiResponse(responseCode = "404", description = "없는 답변일 때", content = @Content)
    })
    public ResponseEntity<MessageResponse> likeToAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 추가할 답변 ID") @PathVariable("answer-id") Long answerId) {

        String userId = userPrincipal.getId(); // 좋아요 누른 사용자 아이디 받아오기
//        String authorUserId = "user_id1"; // 좋아요 누른 사용자 아이디 받아오기
        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER", answerId); // 도메인으로의 의존성을 피하기 위해 문자열로 넘겨줌
        answerService.addLikeToAnswer(request); // 해당 글에 좋아요 1 카운트

        return ResponseEntity.ok(new MessageResponse("좋아요가 추가되었습니다."));
    }

    /**
     * 답변 좋아요 취소
     */
    @PostMapping("/{answer-id}/cancel-likes")
    @Operation(summary = "답변 좋아요 취소", description = "현재 인증된 사용자로 답변 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "좋아요를 누른적이 없는 글일 때", content = @Content),
            @ApiResponse(responseCode = "404", description = "없는 답변일 때", content = @Content)
    })
    public ResponseEntity<MessageResponse> cancelLikeToAnswer(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 취소할 답변 ID") @PathVariable("answer-id") Long answerId) {

        String userId = userPrincipal.getId(); // 좋아요 누른 사용자 아이디 받아오기
//        String reqUserId = "user_id1"; // 좋아요 누른 사용자 아이디 받아오기

        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER", answerId); // 도메인으로의 의존성을 피하기 위해 문자열로 넘겨줌

        answerService.cancelLikeToAnswer(request); // 해당 글에 좋아요 1 마이너스

        return ResponseEntity.ok(new MessageResponse("좋아요가 취소되었습니다."));
    }

}