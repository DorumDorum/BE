package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateCommentRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.CommentResponse;
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
public class UpdateCommentUseCase {

    private final UserService userService;
    private final CommentService commentService;

    @Transactional
    public CommentResponse execute(Long userNo, UpdateCommentRequest request) {
        userService.validateExistsById(userNo);

        Comment comment = commentService.findActiveById(request.commentNo());

        if (!comment.isWriter(userNo))
            throw new RestApiException(GlobalErrorStatus.NO_PERMISSION_ON_NOTICE);
        if (request.content() == null || request.content().isEmpty())
            throw new RestApiException(GlobalErrorStatus.CONTENT_IS_EMPTY);

        Comment updated = commentService.updateComment(comment, request.content());

        return CommentResponse.create(updated);
    }
}

