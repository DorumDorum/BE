package com.project.dorumdorum.domain.friend.application.dto.response;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;

import lombok.Builder;

@Builder
public record LoadFriendRequestResponse(
        Long requestNo,
        Long fromUser,
        Long toUser,
        FriendRequestStatus status
) {
    public static LoadFriendRequestResponse create(FriendRequest request) {
        return LoadFriendRequestResponse.builder()
                .requestNo(request.getFriendRequestNo())
                .fromUser(request.getFromUser())
                .toUser(request.getToUser())
                .status(request.getStatus())
                .build();
    }
}
