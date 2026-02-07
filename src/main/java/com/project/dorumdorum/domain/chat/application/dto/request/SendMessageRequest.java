package com.project.dorumdorum.domain.chat.application.dto.request;

import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @Size(max = 128, message = "initMessage는 128자 이하여야 합니다.") String initMessage
) {}
