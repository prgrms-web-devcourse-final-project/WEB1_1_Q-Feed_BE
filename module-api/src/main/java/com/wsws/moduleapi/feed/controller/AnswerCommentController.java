package com.wsws.moduleapi.feed.controller;

import com.wsws.moduleapi.feed.dto.answer_comment.AnswerCommentPostApiRequest;
import com.wsws.moduleapplication.feed.service.AnswerCommentService;
import com.wsws.modulesecurity.security.UserPrincipal;
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
    public ResponseEntity<?> postAnswerComment(@AuthenticationPrincipal UserPrincipal userPrincipal, AnswerCommentPostApiRequest request) {
        return null;
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
