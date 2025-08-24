package com.project.dorumdorum.domain.notice.application.dto.request;

public record WriteNoticeRequest(
        String title,
        String content,
        Long roomNo
) {

}
