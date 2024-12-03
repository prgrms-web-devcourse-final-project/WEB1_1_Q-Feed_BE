package com.wsws.moduleapplication.user.service;

import com.wsws.moduleapplication.user.dto.LikeServiceRequest;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.user.Like;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.LikeRepository;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.TargetType;
import com.wsws.moduledomain.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    /**
     * Like 저장 생성 및 저장
     */
    public void createLike(LikeServiceRequest request) {

        User user = userRepository.findById(UserId.of(request.userId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);// 연관 맺을 User 찾아오기

        Like like = Like.create(
                null,
                TargetType.valueOf(request.targetType()),
                request.targetId(),
                request.userId()
        );
        try {
            likeRepository.save(like, user);
        } catch (RuntimeException e) { // 연관관계 맺는 과정 중 예외처리
            throw UserNotFoundException.EXCEPTION;
        }

    }

}
