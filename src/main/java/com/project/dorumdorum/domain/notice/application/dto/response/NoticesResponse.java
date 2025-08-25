package com.project.dorumdorum.domain.notice.application.dto.response;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NoticesResponse(
        Long noticeNo,
        Long userNo,
        LocalDateTime updatedAt,
        String title
) {
    public static NoticesResponse create(Notice notice) {
        return NoticesResponse.builder()
                .noticeNo(notice.getNoticeNo())
                .userNo(notice.getUserNo())
                .updatedAt(notice.getUpdatedAt())
                .title(notice.getTitle())
                .build();
    }
}
