package com.project.dorumdorum.domain.friend.application.dto.response;

import com.project.dorumdorum.domain.friend.domain.entity.FriendRequest;
import com.project.dorumdorum.domain.friend.domain.entity.FriendRequestStatus;

import java.util.List;
import java.util.stream.Collectors;

public record FriendRequestListResponse(
        Long requestNo,
        Long fromUser,
        Long toUser,
        FriendRequestStatus status
) {
    public static List<FriendRequestListResponse> create(List<FriendRequest> requests) {
        return requests.stream().map(request -> new FriendRequestListResponse(
                request.getFriendRequestNo(),
                request.getFromUser().getUserNo(),
                request.getToUser().getUserNo(),
                request.getStatus()
        )).collect(Collectors.toList());
    }
}
