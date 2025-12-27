package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import com.project.dorumdorum.domain.notice.service.CommentService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCommentUseCase {

    private final UserService userService;
    private final CommentService commentService;

    @Transactional
    public void execute(Long userNo, Long commentNo) {
        userService.validateExistsById(userNo);

        Comment comment = commentService.findActiveById(commentNo);

        if (!comment.isWriter(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);

        commentService.softDelete(comment);
    }
}

