package com.wsws.moduleapi.socialnetwork.follow.controller;

import com.wsws.moduleapi.socialnetwork.follow.dto.FollowResponseDto;
import com.wsws.moduleapplication.socialnetwork.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.socialnetwork.follow.FollowReadService;
import com.wsws.moduleapplication.socialnetwork.follow.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;
    private final FollowReadService followReadService;

    //팔로우 정보 가져오기
    @Operation(summary = "팔로워 목록 조회", description = "특정 사용자의 팔로워 목록을 조회합니다.")
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowResponseDto>> getFollowers(
            @Parameter(description = "팔로워 목록을 조회할 사용자 ID") @PathVariable String userId,
            @Parameter(description = "커서로 사용할 마지막 팔로워의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String cursor,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        LocalDateTime parsedCursor = cursor != null ? LocalDateTime.parse(cursor) : null;

        List<FollowResponseDto> followers = followReadService.getFollowersWithCursor(userId, parsedCursor, size)
                .stream()
                .map(FollowResponseDto::fromServiceDto) // 응답 변환
                .toList();

        return ResponseEntity.ok(followers);
    }
    @Operation(summary = "팔로잉 목록 조회", description = "특정 사용자의 팔로잉 목록을 조회합니다.")
    @GetMapping("/followings/{userId}")
    public ResponseEntity<List<FollowResponseDto>> getFollowings(
            @Parameter(description = "팔로잉 목록을 조회할 사용자 ID") @PathVariable String userId,
            @Parameter(description = "커서로 사용할 마지막 팔로잉의 시간", example = "2024-01-01T00:00:00") @RequestParam(required = false) String cursor,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size) {
        LocalDateTime parsedCursor = cursor != null ? LocalDateTime.parse(cursor) : null;

        List<FollowResponseDto> followings = followReadService.getFollowingsWithCursor(userId, parsedCursor, size)
                .stream()
                .map(FollowResponseDto::fromServiceDto) // 응답 변환
                .toList();

        return ResponseEntity.ok(followings);
    }
    //팔로우 요청
    @Operation(summary = "팔로우 요청", description = "특정 사용자를 팔로우합니다.")
    @PostMapping
    public ResponseEntity<String> followUser(@RequestBody FollowServiceRequestDto followServiceRequestDto) {
        followService.followUser(followServiceRequestDto);
        return ResponseEntity.status(201).body("팔로우 요청이 완료되었습니다.");
    }


    //언팔로우 요청
    @Operation(summary = "팔로우 취소", description = "특정 사용자를 언팔로우합니다.")
    @DeleteMapping
    public ResponseEntity<String> unfollowUser(@RequestBody FollowServiceRequestDto followServiceRequestDto) {
        followService.unfollowUser(followServiceRequestDto);
        return ResponseEntity.status(201).body("팔로우가 취소되었습니다.");
    }


    //팔로우 여부 체크
    @Operation(summary = "팔로우 상태 확인", description = "특정 사용자 간 팔로우 상태를 확인합니다.")
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFollowing(
            @RequestParam String followerId,
            @RequestParam String followeeId
    ){
        boolean isFollowing = followService.isFollowing(followerId, followeeId);
        return ResponseEntity.ok(isFollowing);
    }


}
