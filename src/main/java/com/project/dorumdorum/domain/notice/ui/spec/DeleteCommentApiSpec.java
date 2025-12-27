package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notice Comment")
public interface DeleteCommentApiSpec {

    @Operation(
            summary = "댓글 삭제",
            description = "댓글 소프트 삭제"
    )
    @DeleteMapping("/api/comment")
    BaseResponse<Void> deleteComment(
            @Parameter(hidden = true)
            Long userNo,
            @Parameter(description = "댓글 번호", required = true)
            @RequestParam Long commentNo
    );
}

