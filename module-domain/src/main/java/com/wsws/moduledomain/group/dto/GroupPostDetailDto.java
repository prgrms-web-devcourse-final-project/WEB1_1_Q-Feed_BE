package com.wsws.moduledomain.group.dto;

import java.time.LocalDateTime;

public record GroupPostDetailDto(
        String authorNickname,  // 작성자 닉네임
        String authorProfile,   // 작성자 프로필 이미지
        String content,         // 게시글 내용
        LocalDateTime createdAt // 작성일
//        List<GroupCommentResponseDto> comments // 댓글 리스트
) {
}
// 아직 구현 안된 부분은 주석 처리로 진행