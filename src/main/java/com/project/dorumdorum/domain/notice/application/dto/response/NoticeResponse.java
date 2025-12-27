package com.project.dorumdorum.domain.notice.application.dto.response;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record NoticeResponse(
        Long noticeNo,
        Long userNo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        String content,
        String imageUploadUrl,
        String imageDownloadUrl,
        String imageFileName,
        Long imageFileSize
) {
    public static NoticeResponse create(Notice notice) {
        return create(notice, null, null, null, null);
    }

    public static NoticeResponse create(Notice notice,
                                        String imageUploadUrl,
                                        String imageDownloadUrl,
                                        String imageFileName,
                                        Long imageFileSize) {
        return NoticeResponse.builder()
                .noticeNo(notice.getNoticeNo())
                .userNo(notice.getUserNo())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .title(notice.getTitle())
                .content(notice.getContent())
                .imageUploadUrl(imageUploadUrl)
                .imageDownloadUrl(imageDownloadUrl)
                .imageFileName(imageFileName)
                .imageFileSize(imageFileSize)
                .build();
    }
}
