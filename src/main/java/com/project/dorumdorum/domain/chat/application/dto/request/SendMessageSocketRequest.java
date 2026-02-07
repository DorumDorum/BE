package com.project.dorumdorum.domain.chat.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageSocketRequest(
        @NotBlank(message = "content는 필수입니다.")
        @Size(max = 1000, message = "content는 1000자 이하여야 합니다.")
        String content
) {}
