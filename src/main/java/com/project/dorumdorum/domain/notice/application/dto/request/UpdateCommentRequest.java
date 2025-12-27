package com.project.dorumdorum.domain.notice.application.dto.request;

public record UpdateCommentRequest(
        Long commentNo,
        String content
) {
}

