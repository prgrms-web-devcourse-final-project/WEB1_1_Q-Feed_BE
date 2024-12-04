package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.answer_comment.AnswerCommentPostApiRequest;
import com.wsws.moduleapi.feed.dto.answer_comment.AnswerCommentPostApiResponse;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer_comment.AnswerCommentCreateServiceResponse;
import com.wsws.moduleapplication.feed.service.AnswerCommentService;
import com.wsws.modulesecurity.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed/comments")
@RequiredArgsConstructor
public class AnswerCommentController {

    private final AnswerCommentService answerCommentService;

    /**
     * 답변 대댓글 조회
     */
    @GetMapping("/{parent-comment-id}")
    public ResponseEntity<?> getAnswerCommentReply() {
        return null;
    }

    /**
     * 답변 댓글 추가
     */
    @PostMapping
    public ResponseEntity<AnswerCommentPostApiResponse> postAnswerComment(
//            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AnswerCommentPostApiRequest request) {
//        String userId = userPrincipal.getId();
        String userId = "user_id1";
        AnswerCommentCreateServiceResponse answerComment =
                answerCommentService.createAnswerComment(request.toServiceDto(userId));
        return ResponseEntity.ok(new AnswerCommentPostApiResponse(answerComment.answerCommentId(), "댓글이 추가되었습니다."));
    }


    /**
     * 답변 댓글 수정
     */
    @PutMapping("/{comment-id}")
    public ResponseEntity<?> putAnswerComment() {
        return null;
    }

    /**
     * 답변 댓글 삭제
     */
    @DeleteMapping("/{comment-id}")
    public ResponseEntity<?> deleteAnswerComment() {
        return null;
    }


    /**
     * 답변 댓글 좋아요
     */
    @PostMapping("/{comment-id}/likes")
    public ResponseEntity<?> addLikeToAnswerComment() {
        return null;
    }


    /**
     * 답변 댓글 좋아요 취소
     */
    @PostMapping("/{comment-id}/cancel-likes")
    public ResponseEntity<?> cancelLikeToAnswerComment() {
        return null;
    }

}
