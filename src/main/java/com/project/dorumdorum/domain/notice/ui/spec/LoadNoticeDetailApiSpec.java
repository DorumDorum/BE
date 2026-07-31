package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Notice", description = "공지사항 API")
public interface LoadNoticeDetailApiSpec {

    @Operation(summary = "공지사항 상세 조회", description = "공지사항을 단건 조회합니다.")
    @GetMapping("/api/notices/{noticeNo}")
    ResponseEntity<NoticeResponse> loadNotice(@PathVariable String noticeNo);
}
