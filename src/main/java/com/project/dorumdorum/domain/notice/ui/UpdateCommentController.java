package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateCommentRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.CommentResponse;
import com.project.dorumdorum.domain.notice.application.usecase.UpdateCommentUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.UpdateCommentApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCommentController implements UpdateCommentApiSpec {

    private final UpdateCommentUseCase updateCommentUseCase;

    @Override
    public BaseResponse<CommentResponse> updateComment(
            @CurrentUser Long userNo,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        return BaseResponse.onSuccess(updateCommentUseCase.execute(userNo, request));
    }
}

