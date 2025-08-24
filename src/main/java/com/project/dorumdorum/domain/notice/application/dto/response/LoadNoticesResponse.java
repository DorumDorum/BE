package com.project.dorumdorum.domain.notice.application.dto.response;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record LoadNoticesResponse(
        Long noticeNo,
        Long userNo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        String content
) {
    public static LoadNoticesResponse create(Notice notice) {
        return LoadNoticesResponse.builder()
                .noticeNo(notice.getNoticeNo())
                .userNo(notice.getUserNo())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .title(notice.getTitle())
                .content(notice.getContent())
                .build();
    }
}
