package com.project.dorumdorum.domain.notice.application.usecase;


import com.project.dorumdorum.domain.notice.application.dto.request.WriteCommentRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.CommentResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Comment;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.service.CommentService;
import com.project.dorumdorum.domain.notice.service.NoticeService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteCommentUseCase {

    private final UserService userService;
    private final NoticeService noticeService;
    private final CommentService commentService;

    public CommentResponse execute(Long userNo, WriteCommentRequest request) {
        userService.validateExistsById(userNo);

        Notice notice = noticeService.findById(request.noticeNo());

        if(request.content().isEmpty())
            throw new RestApiException(GlobalErrorStatus.CONTENT_IS_EMPTY);
        Comment comment = commentService.saveComment(userNo, notice.getNoticeNo(), request.content());

        return CommentResponse.create(comment);
    }
}
