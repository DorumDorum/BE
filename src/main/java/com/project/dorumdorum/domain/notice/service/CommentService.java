package com.project.dorumdorum.domain.notice.service;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import com.project.dorumdorum.domain.notice.domain.repository.CommentRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment saveComment(Long userNo, Long noticeNo, String content) {
        Comment entity = Comment.builder()
                .userNo(userNo)
                .noticeNo(noticeNo)
                .content(content)
                .build();
        return commentRepository.save(entity);
    }

    public List<Comment> findByNoticeNo(Long noticeNo) {
        return commentRepository.findByNoticeNoAndDeletedAtIsNull(noticeNo);
    }

    public Comment findActiveById(Long commentNo) {
        Comment comment = commentRepository.findById(commentNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));

        if (comment.isDeleted())
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);

        return comment;
    }

    public Comment updateComment(Comment comment, String content) {
        comment.updateContent(content);
        return comment;
    }

    public void softDelete(Comment comment) {
        comment.delete();
        commentRepository.save(comment);
    }
}
