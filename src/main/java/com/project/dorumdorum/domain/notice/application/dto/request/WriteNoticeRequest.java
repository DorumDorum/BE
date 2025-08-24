package com.project.dorumdorum.domain.notice.application.dto.request;

public record WriteNoticeRequest(
        Long roomNo,
        String title,
        String content
) {

}
