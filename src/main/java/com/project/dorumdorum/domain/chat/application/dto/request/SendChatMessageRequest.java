package com.project.dorumdorum.domain.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendChatMessageRequest(
        @NotBlank String senderNo,
        @NotBlank String recipientNo,
        @NotBlank String content
) {
}
