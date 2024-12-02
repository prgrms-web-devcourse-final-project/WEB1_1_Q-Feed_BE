package com.wsws.moduleinfra.repo.follow.dto;


import com.wsws.moduledomain.user.vo.Nickname;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponseInfraDto {
    private String userId;
    private String nickname;
    private String profileImage;
    private LocalDateTime createdAt;
}
