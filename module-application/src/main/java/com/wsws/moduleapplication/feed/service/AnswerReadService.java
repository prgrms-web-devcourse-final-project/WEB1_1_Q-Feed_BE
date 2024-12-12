package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer.read.*;
import com.wsws.moduleapplication.feed.dto.answer.read.AnswerCommentFindServiceResponse.AnswerCommentFindServiceResponseBuilder;
import com.wsws.moduleapplication.feed.dto.answer.read.AnswerFindServiceResponse.AnswerFindServiceResponseBuilder;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import com.wsws.moduledomain.feed.like.Like;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.feed.like.LikeRepository;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
     * 답변 목록 조회 (무한 스크롤 페이징 적용)
     */
    public AnswerListFindServiceResponse findAnswerListWithCursor(AnswerFindServiceRequest request) {

        // 답변 리스트 페이징해서 불러오기
        List<Answer> answers = answerRepository.findAllByCategoryIdWithCursor(request.cursor(), request.size(), request.categoryId());

        List<AnswerFindServiceResponse> responses = new ArrayList<>();

        for (Answer answer : answers) {
            AnswerFindServiceResponseBuilder responseBuilder = AnswerFindServiceResponse.builder();

            buildAnswer(answer, request.userId(), responseBuilder); // 답변 정보 세팅

            buildCommentCount(answer, responseBuilder); // 해당 답변의 (최상위)부모 댓글 갯수 추가

            responses.add(responseBuilder.build()); // 리스트에 추가
        }

        return new AnswerListFindServiceResponse(responses);
    }


    /**
     * 답변 상세 조회 (댓글도 함께 받아오며, 댓글은 페이징 처리)
     */
    public AnswerFindServiceResponse findOneAnswerWithCursor(AnswerFindServiceRequest request) {
        AnswerFindServiceResponseBuilder answerResponseBuilder = AnswerFindServiceResponse.builder();

        // 답변 정보 받아오기
        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);

        // Answer 정보 세팅
        buildAnswer(answer, request.userId(), answerResponseBuilder);

        // Answer에 대한 부모 Answer Comment를 페이징으로 가져오기
        List<AnswerComment> parentComments = answerCommentRepository.findParentCommentsByAnswerIdWithCursor(request.answerId(), request.cursor(), request.size());

        // AnswerComment 정보 세팅
        buildAnswerComment(parentComments, request.userId(), answerResponseBuilder);

        return answerResponseBuilder.build();
    }


    /**
     * 특정 사용자의 답변 목록 (페이징 적용)
     */
    public AnswerListFindByUserServiceResponse findAnswerListByUserWithCursor(AnswerFindByUserServiceRequest request) {
        validateTargetUser(request); // 대상 사용자 존재여부 검증

        boolean isMine = isMine(request); // 조회 요청한 사용자와 대상자가 같은지

        List<AnswerQuestionDTO> answers =
                answerRepository.findAllByUserIdWithCursor(request.targetUserId(), request.cursor(), request.size(), isMine); // 페이징으로 Answer 및 Question 정보 가져오기

        List<AnswerFindByUserServiceResponse> serviceResponses = answers.stream()
                .map(AnswerFindByUserServiceResponse::toServiceResponse)
                .toList();

        return new AnswerListFindByUserServiceResponse(serviceResponses);
    }


    /**
     * 특정 사용자의 답변 갯수
     */
    public AnswerCountByUserServiceResponse countAnswersByUser(AnswerFindByUserServiceRequest request) {
        validateTargetUser(request);

        boolean isMine = isMine(request); // 조회 요청한 사용자와 대상자가 같은지

        Long answerCount = answerRepository.countByUserId(request.targetUserId(), isMine);
        return new AnswerCountByUserServiceResponse(answerCount);
    }

    /**
     * 현재 사용자의 오늘의 질문에 대한 답변
     */
    public Optional<AnswerFindByUserAndDailyQuestionServiceResponse> findAnswerByUserAndDailyQuestion(AnswerFindByUserAndDailyQuestionServiceRequest request) {

        Optional<Answer> answer = answerRepository.findAnswerByUserIdAndQuestionId(request.reqUserId(), request.questionId());

        return answer.map(a ->
                new AnswerFindByUserAndDailyQuestionServiceResponse(
                        a.getAnswerId().getValue(),
                        a.getContent(),
                        a.getCreatedAt()
                )
        );
    }







    /* Private Method */

    /**
     * Answer 정보를 세팅
     */
    private void buildAnswer(Answer answer, String reqUserId, AnswerFindServiceResponseBuilder answerResponseBuilder) {

        // 해당 답변을 작성한 사용자 정보 받아오기
        User answerAuthor = userRepository.findById(answer.getUserId())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        // 해당 사용자가 해당 답변에 좋아요를 눌렀는지
        boolean isLike = buildIsLike(reqUserId, answer.getAnswerId().getValue());

        // 해당 사용자가 특정 작성자(작성자 ID)를 팔로우 했는지 확인
        boolean isFollowing = buildIsFollowing(reqUserId, answerAuthor.getId().getValue());

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
    private void buildAnswerComment(List<AnswerComment> parentComments, String reqUserId, AnswerFindServiceResponseBuilder answerResponseBuilder) {

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
                        reqUserId,
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
    private void buildParentComment(AnswerCommentFindServiceResponseBuilder commentResponseBuilder, AnswerComment parent, String reqUserId) {

        // 해당 답변을 작성한 사용자 정보 받아오기
        User commentAuthor = userRepository.findById(UserId.of(parent.getUserId().getValue()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        boolean isLike = buildIsLike(reqUserId, parent.getParentAnswerCommentId().getValue());

        boolean isFollowing = buildIsFollowing(reqUserId, commentAuthor.getId().getValue());

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
            String reqUserId,
            AnswerComment parent,
            Map<Long, List<AnswerComment>> childrenMap,
            Set<Long> processedComments) {

        // 현재 댓글 ID를 처리된 Set에 추가
        processedComments.add(parent.getAnswerCommentId().getValue());

        // 부모 댓글 정보 설정
        buildParentComment(commentResponseBuilder, parent, reqUserId);

        // 자식 댓글 가져오기
        List<AnswerComment> children = childrenMap.get(parent.getAnswerCommentId().getValue());

        if (children != null && !children.isEmpty()) {
            // 자식 댓글이 있으면 재귀적으로 자식 댓글 처리
            commentResponseBuilder.childCommentCount(children.size());
            commentResponseBuilder.children(
                    children.stream()
                            .map(child -> buildCommentHierarchy(
                                    AnswerCommentFindServiceResponse.builder(),
                                    reqUserId,
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

    /**
     * 특정 답변의 (최상위)부모 댓글 갯수 정보 세팅
     */
    private void buildCommentCount(Answer answer, AnswerFindServiceResponseBuilder responseBuilder) {
        int commentCount = answerCommentRepository.countParentCommentByAnswerId(answer.getAnswerId().getValue());
        responseBuilder.commentCount(commentCount);
    }

    /**
     * 조회 요청한 사용자와 대상자가 같은지
     */
    private boolean isMine(AnswerFindByUserServiceRequest request) {
        boolean isMine = request.reqUserId().equals(request.targetUserId());
        return isMine;
    }

    /**
     * 대상 사용자가 존재하는지 검증
     */
    private void validateTargetUser(AnswerFindByUserServiceRequest request) {
        userRepository.findById(UserId.of(request.targetUserId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION); // 대상 사용자가 존재하는지 검증
    }
}
