package com.wsws.moduleapplication.feed.service;

import com.wsws.moduleapplication.feed.dto.answer.read.*;
import com.wsws.moduleapplication.feed.dto.answer.read.AnswerCommentFindServiceResponse.AnswerCommentFindServiceResponseBuilder;
import com.wsws.moduleapplication.feed.dto.answer.read.AnswerFindServiceResponse.AnswerFindServiceResponseBuilder;
import com.wsws.moduleapplication.feed.exception.AnswerNotFoundException;
import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.feed.answer.Answer;
import com.wsws.moduledomain.feed.answer.repo.AnswerRepository;
import com.wsws.moduledomain.feed.answer.vo.AnswerId;
import com.wsws.moduledomain.feed.comment.AnswerComment;
import com.wsws.moduledomain.feed.comment.repo.AnswerCommentRepository;
import com.wsws.moduledomain.feed.dto.AnswerCommentCountDTO;
import com.wsws.moduledomain.feed.dto.AnswerQuestionDTO;
import com.wsws.moduledomain.feed.like.TargetType;
import com.wsws.moduledomain.socialnetwork.follow.aggregate.Follow;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowRepository;
import com.wsws.moduledomain.feed.like.Like;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.feed.like.LikeRepository;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.wsws.moduledomain.feed.like.TargetType.ANSWER;
import static com.wsws.moduledomain.feed.like.TargetType.ANSWER_COMMENT;

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

        // 답변 리스트 정보 세팅
        List<AnswerFindServiceResponse> responses = new ArrayList<>();
        buildAnswerList(request, responses);

        return new AnswerListFindServiceResponse(responses);
    }

    /**
     * 답변 상세 조회 (댓글도 함께 받아오며, 댓글은 페이징 처리)
     */
    public AnswerFindServiceResponse findOneAnswerWithCursor(AnswerFindServiceRequest request) {
        AnswerFindServiceResponseBuilder builder = AnswerFindServiceResponse.builder();

        buildSingleAnswer(request, builder); // 답변 응답 정보 세팅

        buildAnswerComment(request, builder);
        return builder.build();
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

    /**
     * 인기 답변 조회
     * 좋아요 수를 기준으로 5개의 인기답변 조회
     */
    public TrendingAnswerListFindServiceResponse findTrendingAnswer(TrendingAnswerFindServiceRequest request) {
        List<TrendingAnswerFindServiceResponse> trendingAnswers =
                answerRepository.findAnswersByLikeCountAndCategoryIdWithCursor(request.categoryId(), request.limit())
                        .stream().map(answer -> new TrendingAnswerFindServiceResponse(answer.getAnswerId().getValue(), answer.getContent()))
                        .toList();
        return new TrendingAnswerListFindServiceResponse(trendingAnswers);
    }


    /* Private Method */
    /**
     * 단일 답변 응답 세팅
     */
    private void buildSingleAnswer(AnswerFindServiceRequest request, AnswerFindServiceResponseBuilder builder) {
        // 답변 정보 조회
        Answer answer = answerRepository.findById(request.answerId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);
        // 답변 정보 세팅
        buildAnswerInfo(answer, builder);

        // 답변 작성자 정보 조회
        User author = userRepository.findById(answer.getUserId())
                .orElseThrow(() -> AnswerNotFoundException.EXCEPTION);
        // 답변 작성자 정보 세팅
        buildAnswerAuthorInfo(author, builder);

        // 좋아요 정보 조회 및 좋아요 여부 세팅
        if (likeRepository.existsByTargetIdAndUserIdAndTargetType(answer.getAnswerId().getValue(), request.userId(), ANSWER))
            builder.isLike(true);

        // 팔로우 정보 조회 및 팔로우 여부 세팅
        if (followRepository.findByFollowerIdAndFolloweeId(request.userId(), answer.getUserId().getValue()).isPresent())
            builder.isFollowing(true);
    }

    /**
     * 답변 목록 응답 세팅
     */
    private void buildAnswerList(AnswerFindServiceRequest request, List<AnswerFindServiceResponse> responses) {

        // 답변 리스트 페이징해서 불러오기
        List<Answer> answers = answerRepository.findAllByCategoryIdWithCursor(request.cursor(), request.size(), request.categoryId());

        // Id만 리스트로 뽑아내기
        List<Long> answerIds = getAnswerIds(answers);

        // 답변 작성자 ID만 리스트로 뽑아내기
        List<String> answerAuthorIds = getAnswerAuthorIds(answers);


        // 답변 관련 쿼리 한번에 실행
        List<User> answerAuthors = userRepository.findUsersByIds(answerAuthorIds); // 작성자 정보 조회
        List<Like> likes = likeRepository.findByTargetIdsInAndTargetTypeAndUserId(answerIds, ANSWER, request.userId()); // 좋아요 정보 조회
        List<Follow> follows = followRepository.findByFollowerIdAndFolloweeIds(request.userId(), answerAuthorIds); // 팔로우 정보 조회
        List<AnswerCommentCountDTO> answerCommentCounts = answerCommentRepository.countCommentsByAnswerIds(answerIds); // 댓글 갯수 조회

        AnswerFindServiceResponseBuilder builder = AnswerFindServiceResponse.builder();
        answers.forEach
                (answer -> {
                    UserId userId = answer.getUserId();
                    AnswerId answerId = answer.getAnswerId();

                    buildAnswerInfo(answer, builder); // 답변 정보 세팅

                    for (User author : answerAuthors) {
                        if (userId.equals(author.getId())) {
                            buildAnswerAuthorInfo(author, builder); // 답변 작성자 정보 세팅
                            break;
                        }
                    }

                    for (Like like : likes) {
                        if (answerId.getValue().equals(like.getTargetId().getValue())) {
                            // 좋아요 관련 정보 세팅
                            builder.isLike(true);
                            break;
                        }
                    }

                    for (Follow follow : follows) {
                        if (userId.getValue().equals(follow.getId().getFolloweeId())) {
                            // 팔로우 관련 정보 세팅
                            builder.isFollowing(true);
                            break;
                        }
                    }

                    for (AnswerCommentCountDTO answerCommentCount : answerCommentCounts) {
                        if (answerId.getValue().equals(answerCommentCount.targetId())) {
                            // 댓글 수 관련 정보 세팅
                            builder.commentCount(answerCommentCount.answerCommentCount());
                            break;
                        }
                    }

                    responses.add(builder.build());
                });
    }

    /**
     * Answer Comment 정보를 세팅 ver2
     */
    private void buildAnswerComment(AnswerFindServiceRequest request, AnswerFindServiceResponseBuilder answerResponseBuilder) {

        // 페이징으로 최상위 부모 댓글 가져오기
        List<AnswerComment> parentComments = answerCommentRepository.findParentCommentsByAnswerIdWithCursor(request.answerId(), request.cursor(), request.size());
        // 최상위 부모댓글과 얽힌 모든 댓글들 DTO 리스트에 추가
        List<AnswerCommentFindServiceResponse> rawAnswerCommentDTOs = new ArrayList<>();
        buildAnswerCommentDTOList(parentComments, request.userId(), rawAnswerCommentDTOs);

        List<Long> commentIds = getCommentIdsFromDTOs(rawAnswerCommentDTOs);
        List<String> commentAuthorIds = getCommentAuthorIdsFromDTOs(rawAnswerCommentDTOs);

        // 모든 댓글 작성자 조회
        List<User> commentAuthors = userRepository.findUsersByIds(commentAuthorIds);
        // 모든 좋아요 정보 조회
        List<Like> likes = likeRepository.findByTargetIdsInAndTargetTypeAndUserId(commentIds, ANSWER_COMMENT, request.userId());
        // 모든 팔로우 정보 조회
        List<Follow> follows = followRepository.findByFollowerIdAndFolloweeIds(request.userId(), commentAuthorIds);

        List<AnswerCommentFindServiceResponse> answerCommentDTOs = new ArrayList<>();

        rawAnswerCommentDTOs.forEach(
                answerCommentDTO -> {
                    AnswerCommentFindServiceResponseBuilder builder = AnswerCommentFindServiceResponse.builder();
                    buildCommentInfo(answerCommentDTO, builder);

                    String userId = answerCommentDTO.userId();
                    Long commentId = answerCommentDTO.commentId();

                    for (User author : commentAuthors) {
                        if (userId.equals(author.getId().getValue())) {
                            // 댓글 작성자 정보 세팅
                            buildCommentAuthorInfo(author, builder);
                            break;
                        }
                    }

                    for (Like like : likes) {
                        if (commentId.equals(like.getTargetId().getValue())) {
                            // 좋아요 관련 정보 세팅
                            builder.isLike(true);
                            break;
                        }
                    }

                    for (Follow follow : follows) {
                        if (userId.equals(follow.getId().getFolloweeId())) {
                            // 팔로우 관련 정보 세팅
                            builder.isFollowing(true);
                            break;
                        }
                    }

                    answerCommentDTOs.add(builder.build());
                }
        );

        // 부모댓글이 있는 댓글을 찾아 부모 댓글의 children 리스트에 해당 댓글 추가
        answerCommentDTOs.stream()
                .filter(child -> child.parentCommentId() != null) // 부모 ID가 있는 경우만 처리
                .forEach(child -> answerCommentDTOs.stream()
                        .filter(parent -> parent.commentId().equals(child.parentCommentId())) // 부모와 매칭
                        .findFirst() // 부모가 존재하는 경우
                        .ifPresent(parent -> parent.children().add(child)) // 자식 추가
                );

        // 부모 댓글에 종속된 중복된 댓글 제거
        List<AnswerCommentFindServiceResponse> results = answerCommentDTOs.stream()
                .filter(answerCommentDTO -> answerCommentDTO.parentCommentId() == null)
                .toList();

        // 대댓글 수 세팅
        buildChildCommentCount(results);

        // 댓글 셋팅
        answerResponseBuilder
                .comments(results);

    }


    /**
     * 모든 댓글들을 DTO 형식으로 세팅
     */
    private void buildAnswerCommentDTOList(List<AnswerComment> parentComments, String reqUserId, List<AnswerCommentFindServiceResponse> commentDTOs) {

        // 부모 댓글 세팅
        for (AnswerComment parentComment : parentComments) {
            commentDTOs.add(buildAnswerCommentDto(reqUserId, parentComment));
        }

        // 부모 Comment ID 추출
        List<Long> parentIds = getCommentIds(parentComments);

        // 부모 Comment에 대한 하위 댓글 조회
        List<AnswerComment> childComments = answerCommentRepository.findChildCommentsByParentsId(parentIds);

        if (childComments.isEmpty()) return;

        // 재귀적으로 호출
        buildAnswerCommentDTOList(childComments, reqUserId, commentDTOs);
    }


    /**
     * 댓글을 DTO 형식으로 세팅
     */
    private AnswerCommentFindServiceResponse buildAnswerCommentDto(String reqUserId, AnswerComment parentComment) {
        AnswerCommentFindServiceResponseBuilder builder = AnswerCommentFindServiceResponse.builder();

        // 댓글 정보 세팅
        builder
                .commentId(parentComment.getAnswerCommentId().getValue())
                .userId(parentComment.getUserId().getValue())
                .content(parentComment.getContent())
                .likeCount(parentComment.getLikeCount())
                .createdAt(parentComment.getCreatedAt())
                .parentCommentId(parentComment.getParentAnswerCommentId().getValue())
                .childCommentCount(new AtomicInteger(0))
                .children(new ArrayList<>());

        return builder.build();
    }

    /**
     *  대댓글 수 세팅
     */
    private void buildChildCommentCount(List<AnswerCommentFindServiceResponse> commentDTOs) {
        for (AnswerCommentFindServiceResponse commentDTO : commentDTOs) {
            calculateAndSetChildCommentCount(commentDTO);
        }
    }
    private int calculateAndSetChildCommentCount(AnswerCommentFindServiceResponse parent) {
        int childCount = 0;

        for (AnswerCommentFindServiceResponse child : parent.children()) {
            childCount += 1; // 직접적인 자식 개수
            childCount += calculateAndSetChildCommentCount(child); // 자식의 자식 개수를 재귀적으로 더함
        }

        parent.changeChildCommentCount(childCount); // 총 자식 개수 설정
        return childCount; // 부모에게 반환
    }
    /**
     * 답변 작성자의 ID 리스트
     */
    private List<String> getAnswerAuthorIds(List<Answer> answers) {
        return answers.stream()
                .map(answer -> answer.getUserId().getValue())
                .toList();
    }

    /**
     * 답변의 ID 리스트
     */
    private List<Long> getAnswerIds(List<Answer> answers) {
        return answers.stream()
                .map(answer -> answer.getAnswerId().getValue())
                .toList();
    }

    /**
     * 댓글의 ID 리스트
     */
    private List<Long> getCommentIds(List<AnswerComment> answerComments) {
        return answerComments.stream()
                .map(answerComment -> answerComment.getAnswerCommentId().getValue())
                .toList();
    }

    /**
     * 댓글의 ID 리스트
     * DTO에서 받아옴
     */
    private List<Long> getCommentIdsFromDTOs(List<AnswerCommentFindServiceResponse> answerCommentDTOs) {
        return answerCommentDTOs.stream()
                .map(AnswerCommentFindServiceResponse::commentId)
                .toList();
    }

    /**
     * 댓글 작성자의 ID 리스트
     * DTO에서 받아옴
     */
    private List<String> getCommentAuthorIdsFromDTOs(List<AnswerCommentFindServiceResponse> answerCommentDTOs) {
        return answerCommentDTOs.stream()
                .map(AnswerCommentFindServiceResponse::userId)
                .toList();
    }

    /**
     * 답변 정보 세팅
     */
    private void buildAnswerInfo(Answer answer, AnswerFindServiceResponseBuilder builder) {
        builder.answerId(answer.getAnswerId().getValue())
                .content(answer.getContent())
                .createdAt(answer.getCreatedAt())
                .likeCount(answer.getLikeCount());
    }

    /**
     * 답변 작성자 정보 세팅
     */
    private void buildAnswerAuthorInfo(User author, AnswerFindServiceResponseBuilder builder) {
        builder.authorUserId(author.getId().getValue())
                .authorNickname(author.getNickname().getValue())
                .profileImage(author.getProfileImage());
    }

    /**
     * 댓글 정보 세팅
     */
    private void buildCommentInfo(AnswerCommentFindServiceResponse answerCommentDTO, AnswerCommentFindServiceResponseBuilder builder) {
        builder
                .commentId(answerCommentDTO.commentId())
                .userId(answerCommentDTO.userId())
                .content(answerCommentDTO.content())
                .likeCount(answerCommentDTO.likeCount())
                .createdAt(answerCommentDTO.createdAt())
                .parentCommentId(answerCommentDTO.parentCommentId())
                .childCommentCount(answerCommentDTO.childCommentCount())
                .children(answerCommentDTO.children());
    }

    /**
     * 댓글 작성자 정보 세팅
     */
    private void buildCommentAuthorInfo(User author, AnswerCommentFindServiceResponseBuilder builder) {
        builder
                .authorNickname(author.getNickname().getValue())
                .profileImage(author.getProfileImage());
    }

    /**
     * 조회 요청한 사용자와 대상자가 같은지
     */
    private boolean isMine(AnswerFindByUserServiceRequest request) {
        return request.reqUserId().equals(request.targetUserId());
    }

    /**
     * 대상 사용자가 존재하는지 검증
     */
    private void validateTargetUser(AnswerFindByUserServiceRequest request) {
        userRepository.findById(UserId.of(request.targetUserId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION); // 대상 사용자가 존재하는지 검증
    }
}
