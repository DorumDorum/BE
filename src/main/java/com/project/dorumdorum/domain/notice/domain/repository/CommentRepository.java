package com.project.dorumdorum.domain.notice.domain.repository;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByNoticeNoAndDeletedAtIsNull(Long noticeNo);
}
