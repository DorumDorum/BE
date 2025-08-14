package com.project.dorumdorum.domain.friend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record FriendRequestRequest(
        @Email @NotBlank String email
) {}
