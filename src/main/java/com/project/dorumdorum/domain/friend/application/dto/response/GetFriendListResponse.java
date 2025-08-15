package com.project.dorumdorum.domain.friend.application.dto.response;

import com.project.dorumdorum.domain.friend.domain.entity.Friendship;

import java.util.List;
import java.util.stream.Collectors;

public record GetFriendListResponse(
        Long friendshipNo,
        Long userNo,
        Long friendUserNo
) {
    public static List<GetFriendListResponse> create(List<Friendship> friendships) {
        return friendships.stream().map(friendship -> new GetFriendListResponse(
                friendship.getFriendshipNo(),
                friendship.getUserNo(),
                friendship.getFriendUserNo()
        )).collect(Collectors.toList());
    }
}
