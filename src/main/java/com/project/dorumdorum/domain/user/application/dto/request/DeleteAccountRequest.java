package com.project.dorumdorum.domain.user.application.dto.request;

import jakarta.validation.constraints.Size;

public record DeleteAccountRequest(
        @Size(max = 100) String reason
) {
}
