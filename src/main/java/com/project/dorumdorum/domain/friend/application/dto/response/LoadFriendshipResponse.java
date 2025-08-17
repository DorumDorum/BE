package com.project.dorumdorum.domain.friend.application.dto.response;

import com.project.dorumdorum.domain.friend.domain.entity.Friendship;
import com.project.dorumdorum.domain.user.domain.entity.User;

import lombok.Builder;

@Builder
public record LoadFriendshipResponse(
        Long friendshipNo,
        String profileImageURL,
        String name,
        String nickname,
        int age,
        int grade,
        String major
) {
    public static LoadFriendshipResponse create(Friendship friendShip, User user) {
        return LoadFriendshipResponse.builder()
                .friendshipNo(friendShip.getFriendshipNo())
                .name(user.getName())
                .nickname(user.getNickname())
                .build();
    }
}
