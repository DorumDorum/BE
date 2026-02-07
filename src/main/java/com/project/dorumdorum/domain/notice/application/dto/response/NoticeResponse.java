package com.project.dorumdorum.domain.notice.application.dto.response;

import java.time.LocalDate;

public record NoticeResponse(
        String noticeNo,
        String title,
        String content,
        LocalDate writtenDate,
        String originalLink
) {
}
