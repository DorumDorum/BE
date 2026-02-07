package com.project.dorumdorum.domain.chat.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record PresenceSignalRequest(
    @NotNull Long roomId
) {
}
