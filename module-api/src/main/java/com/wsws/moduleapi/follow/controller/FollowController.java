package com.wsws.moduleapi.follow.controller;

import com.wsws.moduleapplication.follow.dto.FollowServiceRequestDto;
import com.wsws.moduleapplication.follow.service.FollowService;
import com.wsws.moduleinfra.repo.follow.FollowReadRepository;
import com.wsws.moduleinfra.repo.follow.dto.FollowResponseInfraDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follows")
public class FollowController {

    private final FollowService followService;
    private final FollowReadRepository readRepository;



    //팔로우 정보 가져오기
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowResponseInfraDto>> getFollowers(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") String cursor,
            @RequestParam(defaultValue = "10") int size) {
        List<FollowResponseInfraDto> followers = readRepository.findFollowersWithCursor(userId, cursor, size);
        return ResponseEntity.ok(followers);
    }

    //팔로잉 정보 가져오기
    @GetMapping("/followings/{userId}")
    public ResponseEntity<List<FollowResponseInfraDto>> getFollowings(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") String cursor,
            @RequestParam(defaultValue = "10") int size
    ){
        List<FollowResponseInfraDto> followings = readRepository.findFollowingsWithCursor(userId, cursor, size);
        return ResponseEntity.ok(followings);
    }

    //팔로우 요청
    @PostMapping
    public ResponseEntity<String> followUser(@RequestBody FollowServiceRequestDto followServiceRequestDto) {
        followService.followUser(followServiceRequestDto);
        return ResponseEntity.status(201).body("팔로우 요청이 완료되었습니다.");
    }


    //언팔로우 요청
    @DeleteMapping
    public ResponseEntity<String> unfollowUser(@RequestBody FollowServiceRequestDto followServiceRequestDto) {
        followService.unfollowUser(followServiceRequestDto);
        return ResponseEntity.status(201).body("팔로우가 취소되었습니다.");
    }


    //팔로우 여부 체크
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFollowing(
            @RequestParam String followerId,
            @RequestParam String followeeId
    ){
        boolean isFollowing = followService.isFollowing(followerId, followeeId);
        return ResponseEntity.ok(isFollowing);
    }


}
