package com.project.dorumdorum.domain.notice.application.dto.request;

public record WriteCommentRequest(
        Long noticeNo,
        String content
) {
}
