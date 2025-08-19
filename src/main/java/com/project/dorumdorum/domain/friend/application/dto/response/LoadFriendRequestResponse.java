package com.project.dorumdorum.domain.friend.application.dto.response;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;

import com.project.dorumdorum.domain.user.domain.entity.User;
import lombok.Builder;

@Builder
public record LoadFriendRequestResponse(
        Long requestNo,
        String profileImageURL,
        String nickname,
        int age,
        int grade,
        String major,
        FriendRequestStatus status
) {
    public static LoadFriendRequestResponse create(FriendRequest request, User user) {
        return LoadFriendRequestResponse.builder()
                .requestNo(request.getFriendRequestNo())
                .nickname(user.getNickname())
                .status(request.getStatus())
                .build();
    }
}
