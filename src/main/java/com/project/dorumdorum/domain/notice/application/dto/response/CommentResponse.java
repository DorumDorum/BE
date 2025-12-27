package com.project.dorumdorum.domain.notice.application.dto.response;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long commentNo,
        Long userNo,
        LocalDateTime updated_at,
        String content
) {
    public static CommentResponse create(Comment comment) {
        return CommentResponse.builder()
                .commentNo(comment.getCommentNo())
                .userNo(comment.getUserNo())
                .updated_at(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }
}
