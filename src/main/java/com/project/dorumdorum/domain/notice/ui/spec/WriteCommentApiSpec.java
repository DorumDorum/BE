package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.request.WriteCommentRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.CommentResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Notice Comment")
public interface WriteCommentApiSpec {

    @Operation(
            summary = "댓글 작성",
            description = "공지에 대한 댓글 등록"
    )
    @PostMapping("/api/comment")
    BaseResponse<CommentResponse> writeComment(
            @Parameter(hidden = true)
            Long userNo,
            @RequestBody(
                    description = """
                            댓글 작성 요청
                            - noticeNo: 공지 번호
                            - content: 댓글 내용
                            """,
                    required = true
            )
            WriteCommentRequest request
    );
}

