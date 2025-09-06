package com.project.dorumdorum.domain.notice.ui;


import com.project.dorumdorum.domain.notice.application.dto.request.WriteCommentRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.CommentResponse;
import com.project.dorumdorum.domain.notice.application.usecase.WriteCommentUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class WriteCommentController {

    private final WriteCommentUseCase writeCommentUseCase;

    @PostMapping("/api/comment")
    public BaseResponse<CommentResponse> writeComment(
            @CurrentUser Long userNo,
            @Valid @RequestBody WriteCommentRequest request
    ) {
        return BaseResponse.onSuccess(writeCommentUseCase.execute(userNo, request));
    }
}
