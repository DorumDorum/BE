package com.project.dorumdorum.domain.friend.domain.entity;

public enum FriendRequestStatus {
    ACCEPTED("친구 요청 수락"),
    REJECTED("친구 요청 거절"),
    PENDING("친구 요청 대기 중");

    private final String description;
    FriendRequestStatus(String description) {
        this.description = description;
    }

    public static String toDesc(FriendRequestStatus friendRequestStatus) {
        return friendRequestStatus.description;
    }
}
