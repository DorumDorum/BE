package com.project.dorumdorum.domain.notice.domain.repository;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
