package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateCommentRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.CommentResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "Notice Comment")
public interface UpdateCommentApiSpec {

    @Operation(
            summary = "댓글 수정",
            description = "댓글 내용 수정"
    )
    @PatchMapping("/api/comment")
    BaseResponse<CommentResponse> updateComment(
            @Parameter(hidden = true)
            Long userNo,
            @RequestBody(
                    description = """
                            댓글 수정 요청
                            - commentNo: 댓글 번호
                            - content: 수정 내용
                            """,
                    required = true
            )
            UpdateCommentRequest request
    );
}

