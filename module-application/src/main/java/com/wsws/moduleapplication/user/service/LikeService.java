//package com.wsws.moduleapplication.user.service;
//
//import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
//import com.wsws.moduleapplication.user.exception.AlreadyLikeException;
//import com.wsws.moduleapplication.user.exception.UserNotFoundException;
//import com.wsws.moduledomain.user.Like;
//import com.wsws.moduledomain.user.User;
//import com.wsws.moduledomain.user.repo.LikeRepository;
//import com.wsws.moduledomain.user.repo.UserRepository;
//import com.wsws.moduledomain.user.vo.TargetType;
//import com.wsws.moduledomain.user.vo.UserId;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class LikeService {
//    private final LikeRepository likeRepository;
//    private final UserRepository userRepository;
//
//    /**
//     * Like 저장 생성 및 저장
//     */
//    public void createLike(LikeServiceRequest request) {
//
//        User user = userRepository.findById(UserId.of(request.userId()))
//                .orElseThrow(() -> UserNotFoundException.EXCEPTION);// 연관 맺을 User 찾아오기
//
//        if(isAlreadyLike(request.targetId(), user)) // 좋아요를 누른적이 있는지 확인
//            throw AlreadyLikeException.EXCEPTION;
//
//
//        Like like = Like.create(
//                null,
//                TargetType.valueOf(request.targetType()),
//                request.targetId(),
//                request.userId()
//        );
//        try {
//            likeRepository.save(like, user);
//        } catch (RuntimeException e) { // 연관관계 맺는 과정 중 예외처리
//            throw UserNotFoundException.EXCEPTION;
//        }
//
//    }
//
//
//    /**
//     * 같은 글에 좋아요를 누른적이 있는지 확인
//     */
//    private boolean isAlreadyLike(Long targetId, User user) {
//        return likeRepository.existsByTargetIdAndUserEntity(targetId, user);
//    }
//}
