package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.global.annotation.AccessToken;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "Notice", description = "공지사항 API")
public interface LoadNoticesApiSpec {

    @Operation(summary = "공지사항 조회", description = "모든 공지사항을 작성일 내림차순으로 조회합니다.")
    @GetMapping("/api/notices")
    BaseResponse<List<NoticeResponse>> loadNotices(
            @AccessToken Long userNo
    );
}
