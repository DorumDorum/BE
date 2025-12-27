package com.project.dorumdorum.domain.notice.application.dto.response;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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
        Long imageFileSize,
        List<CommentResponse> comments
) {
    public static NoticeResponse create(Notice notice) {
        return create(notice, Collections.emptyList(), null, null, null, null);
    }

    public static NoticeResponse create(Notice notice, List<CommentResponse> comments) {
        return create(notice, comments, null, null, null, null);
    }

    public static NoticeResponse create(Notice notice,
                                        List<CommentResponse> comments,
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
                .comments(comments == null ? Collections.emptyList() : comments)
                .build();
    }
}
