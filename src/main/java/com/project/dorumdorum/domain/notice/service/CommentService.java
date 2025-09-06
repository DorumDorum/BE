package com.project.dorumdorum.domain.notice.service;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import com.project.dorumdorum.domain.notice.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
