package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer.AnswerCommentFindServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerCommentFindServiceResponse.AnswerCommentFindServiceResponseBuilder;
import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceRequest;
import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceResponse;
import com.wsws.moduleapplication.feed.dto.answer.AnswerFindServiceResponse.AnswerFindServiceResponseBuilder;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AnswerReadService {

    private final AnswerRepository answerRepository;
    private final AnswerCommentRepository answerCommentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    /**
     * 답변 목록 조회
     */
    public void findAnswerListWithCursor() {

    }


    /**
     * <h3> 답변 상세 조회 </h3>
     * -- 필요한 정보 --
     * <h5> 1. 답변 관련 </h5>
     * - User(답변 작성자): authorUserId, nickname, profileImage <br>
     * - Answer: answerId, content, createdAt, likeCount <br>
     * - Like: 해당 사용자가 해당 답변에 좋아요를 눌렀는지 여부 <br>
     * - Follow: 요청한 사용자가 답변 작성자를 팔로우 했는지 여부 <br>
     * <h5> 2. 댓글 관련 </h5> <
     * - User(댓글 작성자): authorUserId, nickname, profileImage<br>
     * - AnswerComment: 댓글 총 갯수(부모 댓글)<br>
     * - AnswerComment: 부모 댓글 리스트(무한스크롤 페이징 적용) - commentId, content, likeCount, childCommentCount, createdAt<br>
     * - AnswerComment: 대 댓글 리스트 - commentId, content, likeCount, createdAt<br>
     * - Like: 해당 사용자가 해당 댓글/대댓글에 좋아요를 눌렀는지 여부<br>
     * - Follow: 요청한 사용자가 댓글/대댓글 작성자를 팔로우 했는지 여부<br>
     */
    public AnswerFindServiceResponse findOneAnswerWithCursor(AnswerFindServiceRequest request) {
        AnswerFindServiceResponseBuilder answerResponseBuilder = AnswerFindServiceResponse.builder();
        // Answer 정보 세팅
        buildAnswer(request, answerResponseBuilder);

        // AnswerComment 정보 세팅
        buildAnswerComment(request, answerResponseBuilder);

        return answerResponseBuilder.build();
    }

    /**
     * Answer 정보를 세팅
     */
    private void buildAnswer(AnswerFindServiceRequest request, AnswerFindServiceResponseBuilder answerResponseBuilder) {
        // 답변 정보 받아오기
        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        // 해당 답변을 작성한 사용자 정보 받아오기
        User answerAuthor = userRepository.findById(UserId.of(answer.getUserId().getValue()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // 해당 사용자가 해당 답변에 좋아요를 눌렀는지
        boolean isLike = buildIsLike(request.userId(), request.answerId());

        // 해당 사용자가 특정 작성자(작성자 ID)를 팔로우 했는지 확인
        boolean isFollowing = buildIsFollowing(request.userId(), answerAuthor.getId().getValue());

        answerResponseBuilder
                .answerId(answer.getAnswerId().getValue())
                .authorUserId(answerAuthor.getId().getValue())
                .authorNickname(answerAuthor.getNickname().getValue())
                .profileImage(answerAuthor.getProfileImage())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .likeCount(answer.getLikeCount())
                .isLike(isLike)
                .isFollowing(isFollowing);
    }

    /**
     * Answer Comment 정보를 세팅
     */
    private void buildAnswerComment(AnswerFindServiceRequest request, AnswerFindServiceResponseBuilder answerResponseBuilder) {
        // Answer에 대한 부모 Answer Comment를 페이징으로 가져오기
        List<AnswerComment> parentComments = answerCommentRepository.findParentCommentsByAnswerIdWithCursor(request.answerId(), request.commentCursor(), request.size());

        // 부모 Comment ID 추출
        List<Long> parentIds = parentComments.stream()
                .map(answerComment -> answerComment.getAnswerCommentId().getValue())
                .toList();

        // 부모 Comment에 대한 하위 댓글 조회
        List<AnswerComment> childComments = answerCommentRepository.findChildCommentsByParentsId(parentIds);

        // 부모 댓글과 자식 댓글 매핑
        Map<Long, List<AnswerComment>> childrenMap = childComments.stream()
                .collect(Collectors.groupingBy(
                        answerComment -> answerComment.getParentAnswerCommentId().getValue()
                ));

        // 중복 제거를 위한 Set : 부모댓글이자 자식댓글도 되는 댓글이 중복추가되는 현상 해결
        Set<Long> processedComments = new HashSet<>();

        // 부모 댓글 기반으로 계층 구조 형성
        List<AnswerCommentFindServiceResponse> commentResponses = parentComments.stream()
                // processedComments에 포함되지 않은 경우만 처리
                .filter(parent -> !processedComments.contains(parent.getAnswerCommentId().getValue()))
                // 조건을 만족하는 경우 계층 구조 빌드
                .map(parent -> buildCommentHierarchy(
                        AnswerCommentFindServiceResponse.builder(),
                        request,
                        parent,
                        childrenMap,
                        processedComments
                ).build())
                .toList();

        answerResponseBuilder
                .commentCount(commentResponses.size())
                .comments(commentResponses);
    }


    /**
     * 좋아요 여부 정보를 세팅
     */
    private boolean buildIsLike(String currentUserId, Long targetId) {
        // 조회 요청한 사용자가 좋아요 누른 답변 및 답변 댓글 정보 다 가져오기
        List<Like> likes = likeRepository.findByUserId(currentUserId);

        // 해당 사용자가 해당 답변에 좋아요를 눌렀는지
        return likes.stream()
                .anyMatch(like -> like.getTargetId().getValue().equals(targetId));
    }

    /**
     * 팔로우 여부 정보를 세팅
     */
    private boolean buildIsFollowing(String currentUserId, String authorId) {
        // 조회 요청한 사용자가 팔로우한 사용자 정보 다 가져오기
        List<Follow> followers = followRepository.findByFollowerId(currentUserId);
        // 해당 사용자가 특정 작성자(작성자 ID)를 팔로우 했는지 확인
        return followers.stream()
                .anyMatch(follow -> follow.getId().getFollowerId().equals(authorId));
    }

    /**
     * 부모 댓글 정보를 세팅
     */
    private void buildParentComment(AnswerCommentFindServiceResponseBuilder commentResponseBuilder, AnswerComment parent, AnswerFindServiceRequest request) {

        // 해당 답변을 작성한 사용자 정보 받아오기
        User commentAuthor = userRepository.findById(UserId.of(parent.getUserId().getValue()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        boolean isLike = buildIsLike(request.userId(), parent.getParentAnswerCommentId().getValue());

        boolean isFollowing = buildIsFollowing(request.userId(), commentAuthor.getId().getValue());

        commentResponseBuilder
                .commentId(parent.getAnswerCommentId().getValue())
                .userId(commentAuthor.getId().getValue())
                .authorNickname(commentAuthor.getNickname().getValue())
                .profileImage(commentAuthor.getProfileImage())
                .content(parent.getContent())
                .likeCount(parent.getLikeCount())
                .createdAt(parent.getCreatedAt())
                .isLike(isLike)
                .isFollowing(isFollowing);
    }

    /**
     * 댓글 계층 정보를 세팅
     */
    private AnswerCommentFindServiceResponseBuilder buildCommentHierarchy(
            AnswerCommentFindServiceResponseBuilder commentResponseBuilder,
            AnswerFindServiceRequest request,
            AnswerComment parent,
            Map<Long, List<AnswerComment>> childrenMap,
            Set<Long> processedComments) {

        // 현재 댓글 ID를 처리된 Set에 추가
        processedComments.add(parent.getAnswerCommentId().getValue());

        // 부모 댓글 정보 설정
        buildParentComment(commentResponseBuilder, parent, request);

        // 자식 댓글 가져오기
        List<AnswerComment> children = childrenMap.get(parent.getAnswerCommentId().getValue());

        if (children != null && !children.isEmpty()) {
            // 자식 댓글이 있으면 재귀적으로 자식 댓글 처리
            commentResponseBuilder.childCommentCount(children.size());
            commentResponseBuilder.children(
                    children.stream()
                            .map(child -> buildCommentHierarchy(
                                    AnswerCommentFindServiceResponse.builder(),
                                    request,
                                    child,
                                    childrenMap,
                                    processedComments
                            ).build())
                            .toList()
            );
        } else {
            // 자식 댓글이 없는 경우
            commentResponseBuilder.childCommentCount(0);
        }

        return commentResponseBuilder;
    }


}
