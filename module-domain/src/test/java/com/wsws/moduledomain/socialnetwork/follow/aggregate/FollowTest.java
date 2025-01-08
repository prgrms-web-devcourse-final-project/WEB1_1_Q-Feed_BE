package com.wsws.moduledomain.socialnetwork.follow.aggregate;

import com.wsws.moduledomain.socialnetwork.follow.exception.InvalidFollowException;
import com.wsws.moduledomain.socialnetwork.follow.exception.InvalidFolloweeIdException;
import com.wsws.moduledomain.socialnetwork.follow.exception.InvalidFollowerIdException;
import com.wsws.moduledomain.socialnetwork.follow.vo.FollowId;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class FollowTest {

    @Test
    void 팔로워와_팔로이의_식별자를_통해_팔로우_식별자를_생성할_수_있다() {
        //given
        String followerId = "followerId";
        String followeeId = "followeeId";

        //when
        FollowId followId = FollowId.of(followerId, followeeId);

        //then
        assertThat(followId.getFollowerId()).isEqualTo(followerId);
        assertThat(followId.getFolloweeId()).isEqualTo(followeeId);
    }

    @Test
    void create_메서드를_통해_유저간의_팔로우를_생성할_수_있다() {
        //given
        String followerId = "followerId";
        String followeeId = "followeeId";

        //when
        Follow follow = Follow.create(followerId, followeeId);

        //then
        assertThat(follow).isNotNull();
        assertThat(follow.getId()).isNotNull();

        assertThat(follow.getId().getFollowerId()).isEqualTo(followerId);
        assertThat(follow.getId().getFolloweeId()).isEqualTo(followeeId);
        assertThat(follow.getCreatedAt()).isNotNull();

//        System.out.println(follow.getCreatedAt());
    }

    @Test
    void of_메서드를_통해_팔로우_객체를_재구성할_수_있다() {
        //given
        String followerId = "followerId";
        String followeeId = "followeeId";
        FollowId followId = FollowId.of(followerId, followeeId);

        //when
        Follow follow = Follow.of(followId, LocalDateTime.now());

        //then
        assertThat(follow).isNotNull();
        assertThat(follow.getId()).isNotNull();
        assertThat(follow.getId().getFollowerId()).isEqualTo(followerId);
        assertThat(follow.getId().getFolloweeId()).isEqualTo(followeeId);
        assertThat(follow.getCreatedAt()).isNotNull();

    }

    @Test
    void 자기_자신을_팔로우하면_에러가_발생한다() {
        //given
        String followerId = "followerId";
        String followeeId = "followerId";

        //then
        assertThatThrownBy(() ->
                Follow.create(followerId, followeeId))
                .isInstanceOf(InvalidFollowException.class);

    }

    @Test
    void FollowId_생성시_followerId가_null이면_에러가_발생한다() {
        // given
        String followerId = null;
        String followeeId = "followeeId";

        // when & then
        assertThatThrownBy(() -> FollowId.of(followerId, followeeId))
                .isInstanceOf(InvalidFollowerIdException.class);
    }

    @Test
    void FollowId_생성시_followeeId가_null이면_에러가_발생한다() {
        // given
        String followerId = "followerId";
        String followeeId = null;

        // when & then
        assertThatThrownBy(() -> FollowId.of(followerId, followeeId))
                .isInstanceOf(InvalidFolloweeIdException.class);
    }

    @Test
    void FollowId_생성시_followerId가_빈문자열이면_에러가_발생한다() {
        // given
        String followerId = "";
        String followeeId = "followeeId";

        // when & then
        assertThatThrownBy(() -> FollowId.of(followerId, followeeId))
                .isInstanceOf(InvalidFollowerIdException.class);
    }

    @Test
    void FollowId_생성시_followeeId가_빈문자열이면_에러가_발생한다() {
        // given
        String followerId = "followerId";
        String followeeId = "";

        // when & then
        assertThatThrownBy(() -> FollowId.of(followerId, followeeId))
                .isInstanceOf(InvalidFolloweeIdException.class);
    }




}