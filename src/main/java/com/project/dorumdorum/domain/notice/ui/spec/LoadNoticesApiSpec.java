package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticesResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Notice")
public interface LoadNoticesApiSpec {

    @Operation(
            summary = "공지 목록 조회",
            description = "방 단위 공지 리스트 조회"
    )
    @GetMapping("/api/notices")
    BaseResponse<List<NoticesResponse>> loadNotices(
            @Parameter(hidden = true)
            Long userNo,
            @Parameter(description = "방 번호", required = true)
            @RequestParam Long roomNo
    );

    @Operation(
            summary = "공지 상세 조회",
            description = "공지 상세 + 댓글 목록 + 이미지 조회 presigned URL 반환"
    )
    @GetMapping("/api/notice")
    BaseResponse<NoticeResponse> loadNotice(
            @Parameter(hidden = true)
            Long userNo,
            @Parameter(description = "공지 번호", required = true)
            @RequestParam Long noticeNo
    );
}

