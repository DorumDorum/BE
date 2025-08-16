package com.project.dorumdorum.domain.friend.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record SendFriendRequest(
        @NotNull Long toUser
) {}
