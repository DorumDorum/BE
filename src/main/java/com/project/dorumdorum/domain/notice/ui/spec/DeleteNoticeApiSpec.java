package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Notice")
public interface DeleteNoticeApiSpec {

    @Operation(
            summary = "공지 삭제",
            description = "공지 소프트 삭제 + 연결 이미지 소프트 삭제, S3 오브젝트는 즉시 삭제"
    )
    @DeleteMapping("/api/notice")
    BaseResponse<Void> deleteNotice(
            @Parameter(hidden = true)
            Long userNo,
            @Parameter(description = "공지 번호", required = true)
            @RequestParam Long noticeNo
    );
}

