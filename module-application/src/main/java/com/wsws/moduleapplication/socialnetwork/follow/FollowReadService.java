package com.wsws.moduleapplication.socialnetwork.follow;

import com.wsws.moduleapplication.socialnetwork.follow.dto.FollowServiceResponseDto;
import com.wsws.moduledomain.socialnetwork.follow.repo.FollowReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowReadService {

    private final FollowReadRepository followReadRepository;

    public List<FollowServiceResponseDto> getFollowersWithCursor(String followeeId, LocalDateTime cursor, int size) {
        LocalDateTime effectiveCursor = cursor != null ? cursor : LocalDateTime.now();

        return followReadRepository.findFollowersWithCursor(followeeId, effectiveCursor, size)
                .stream()
                .map(queryResult -> new FollowServiceResponseDto(
                        queryResult.getUserId(),
                        queryResult.getNickname(),
                        queryResult.getProfileImage(),
                        queryResult.getCreatedAt()
                ))
                .toList();
    }

    public List<FollowServiceResponseDto> getFollowingsWithCursor(String followerId, LocalDateTime cursor, int size) {
        LocalDateTime effectiveCursor = cursor != null ? cursor : LocalDateTime.now();

        return followReadRepository.findFollowingsWithCursor(followerId, effectiveCursor, size)
                .stream()
                .map(queryResult -> new FollowServiceResponseDto(
                        queryResult.getUserId(),
                        queryResult.getNickname(),
                        queryResult.getProfileImage(),
                        queryResult.getCreatedAt()
                ))
                .toList();
    }
}
