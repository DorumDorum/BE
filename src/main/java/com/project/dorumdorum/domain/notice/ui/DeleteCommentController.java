package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.usecase.DeleteCommentUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.DeleteCommentApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteCommentController implements DeleteCommentApiSpec {

    private final DeleteCommentUseCase deleteCommentUseCase;

    @Override
    public BaseResponse<Void> deleteComment(
            @CurrentUser Long userNo,
            @RequestParam Long commentNo
    ) {
        deleteCommentUseCase.execute(userNo, commentNo);
        return BaseResponse.onSuccess();
    }
}

