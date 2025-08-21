package com.project.dorumdorum.domain.friend.application.dto.request;

import jakarta.validation.constraints.NotNull;

public enum FriendRequestType {
    RECEIVED("받은 친구 요청 목록"),
    SENT("보낸 친구 요청 목록");

    private final String description;
    FriendRequestType(@NotNull String description) { this.description = description; }
}
