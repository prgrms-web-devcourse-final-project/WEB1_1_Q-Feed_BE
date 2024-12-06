package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.MessageResponse;
import com.wsws.moduleapi.feed.dto.answer_comment.AnswerCommentPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer_comment.AnswerCommentPostApiResponse;
import com.wsws.moduleapi.feed.dto.answer_comment.AnswerCommentPutApiRequest;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceResponse;
import com.wsws.moduleapplication.feed.service.AnswerCommentService;
import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
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

@RestController
@RequestMapping("/feed/comments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Security 적용
public class AnswerCommentController {

    private final AnswerCommentService answerCommentService;

    /**
     * 답변 댓글 추가
     */
    @Operation(summary = "답변 댓글 추가", description = "답변에 대한 댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "답변 댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "Answer Id가 누락됨", content = @Content),
            @ApiResponse(responseCode = "404", description = "부모 댓글이 요청으로 들어왔는데 해당 댓글이 없는 경우", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AnswerCommentPostApiResponse> postAnswerComment(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AnswerCommentPostApiRequest request) {
//        String userId = userPrincipal.getId();
        String userId = "user_id1";
        AnswerCommentCreateServiceResponse answerComment =
                answerCommentService.createAnswerComment(request.toServiceDto(userId));
        return ResponseEntity.status(201).body(new AnswerCommentPostApiResponse(answerComment.answerCommentId(), "댓글이 추가되었습니다."));
    }


    /**
     * 답변 댓글 수정
     */
    @Operation(summary = "답변 댓글 수정", description = "답변에 대한 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 댓글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 답변 댓글이 없는 경우", content = @Content)
    })
    @PutMapping("/{comment-id}")
    public ResponseEntity<MessageResponse> putAnswerComment(
            @Parameter(description = "수정할 답변 댓글 ID") @PathVariable("comment-id") Long commentId,
            @RequestBody AnswerCommentPutApiRequest request) {
        answerCommentService.editAnswerComment(request.toServiceDto(commentId));
        return ResponseEntity.ok(new MessageResponse("댓글이 수정되었습니다."));
    }

    /**
     * 답변 댓글 삭제
     */
    @Operation(summary = "답변 댓글 삭제", description = "답변에 대한 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 댓글 삭제 성공")
    })
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<MessageResponse> deleteAnswerComment(
            @Parameter(description = "삭제할 답변 댓글 ID") @PathVariable("comment-id") Long commentId) {
        answerCommentService.deleteAnswerComment(commentId);
        return ResponseEntity.ok(new MessageResponse("댓글이 삭제되었습니다."));
    }


    /**
     * 답변 댓글 좋아요
     */
    @Operation(summary = "답변 댓글 좋아요 추가", description = "답변 댓글에 좋아요를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 댓글 좋아요 추가 성공"),
            @ApiResponse(responseCode = "400", description = "이미 좋아요를 누른적이 있는 댓글일 때"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글일 때")
    })
    @PostMapping("/{comment-id}/likes")
    public ResponseEntity<MessageResponse> addLikeToAnswerComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 추가할 답변 댓글 ID") @PathVariable("comment-id") Long commentId) {
        String userId = userPrincipal.getId();
//                String authorUserId = "user_id1";
        answerCommentService.addLikeToAnswerComment(new LikeServiceRequest(userId, "ANSWER_COMMENT", commentId));

        return ResponseEntity.ok(new MessageResponse("좋아요가 추가되었습니다."));
    }


    /**
     * 답변 댓글 좋아요 취소
     */
    @Operation(summary = "답변 댓글 좋아요 취소", description = "현재 인증된 사용자로 답변 댓글 좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "답변 댓글 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "좋아요를 누른적이 없는 답변 댓글일 때", content = @Content),
            @ApiResponse(responseCode = "404", description = "없는 답변 댓글 일 때", content = @Content)
    })
    @PostMapping("/{comment-id}/cancel-likes")
    public ResponseEntity<MessageResponse> cancelLikeToAnswerComment(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "좋아요를 취소할 답변 댓글 ID") @PathVariable("comment-id") Long commentId
    ) {
        String userId = userPrincipal.getId();
//        String authorUserId = "user_id1";
        LikeServiceRequest request = new LikeServiceRequest(userId, "ANSWER_COMMENT", commentId);
        answerCommentService.cancelLikeToCommentAnswer(request);

        return ResponseEntity.ok(new MessageResponse("좋아요가 취소되었습니다."));
    }

}
