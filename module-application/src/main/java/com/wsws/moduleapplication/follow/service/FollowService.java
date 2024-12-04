package com.wsws.moduleapplication.follow.service;

import com.wsws.moduleapplication.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.follow.exception.AlreadyFollowedException;
import com.wsws.moduleapplication.follow.exception.FollowNotFoundException;
import com.wsws.moduleapplication.user.exception.UserNotFoundException;
import com.wsws.moduledomain.follow.Follow;
import com.wsws.moduledomain.follow.repo.FollowRepository;
import com.wsws.moduledomain.notification.Notification;
import com.wsws.moduledomain.notification.repo.NotificationRepository;
import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.UserId;
import com.wsws.moduleexternalapi.fcm.dto.fcmRequestDto;
import com.wsws.moduleexternalapi.fcm.service.FcmServiceImpl;
import com.wsws.moduleexternalapi.fcm.util.FcmType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationRepository notificationRepository;
    private final FcmServiceImpl fcmService;

    // 팔로우
    @Transactional
    public void followUser(FollowServiceRequestDto followServiceRequestDto) {
        if (followRepository.findByFollowerIdAndFolloweeId(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId()).isPresent()) {
            throw AlreadyFollowedException.EXCEPTION;
        }
        Follow follow = Follow.create(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId());

        User followerUser = userRepository.findById(UserId.of(followServiceRequestDto.followerId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        User followeeUser = userRepository.findById(UserId.of(followServiceRequestDto.followeeId()))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        String title = fcmService.makeFcmTitle(FcmType.FOLLOW.getType());
        String body = fcmService.makeFollowBody(
                followeeUser.getNickname().getValue(), FcmType.FOLLOW.getType()
        );
        fcmRequestDto fcmDTO = fcmService.makeFcmDTO(title, body);

        // 알림 저장
        Notification notice = Notification.builder()
                .type(FcmType.FOLLOW.getType())
                .sender(followeeUser.getNickname().getValue())
                .recipient(followerUser.getNickname().getValue())
                .build();
        notificationRepository.save(notice);

        //fcm 전송
        fcmService.fcmSend(followerUser.getNickname().getValue(), fcmDTO);

        followRepository.save(follow);
    }

    // 언팔로우
    public void unfollowUser(FollowServiceRequestDto followServiceRequestDto) {
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followServiceRequestDto.followerId(), followServiceRequestDto.followeeId())
                .orElseThrow(() -> FollowNotFoundException.EXCEPTION);
        followRepository.delete(follow);
    }

    // 팔로우 여부 체크
    public boolean isFollowing(String followerId, String followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent();
    }
}
