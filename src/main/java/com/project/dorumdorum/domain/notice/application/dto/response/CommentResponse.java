package com.project.dorumdorum.domain.notice.application.dto.response;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long noticeNo,
        Long userNo,
        LocalDateTime updated_at,
        String content
) {
    public static CommentResponse create(Comment comment) {
        return CommentResponse.builder()
                .noticeNo(comment.getNoticeNo())
                .userNo(comment.getUserNo())
                .updated_at(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }
}
