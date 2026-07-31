package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticeDetailUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.LoadNoticeDetailApiSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadNoticeDetailController implements LoadNoticeDetailApiSpec {

    private final LoadNoticeDetailUseCase loadNoticeDetailUseCase;

    @Override
    public ResponseEntity<NoticeResponse> loadNotice(@PathVariable String noticeNo) {
        return ResponseEntity.ok(loadNoticeDetailUseCase.execute(noticeNo));
    }
}
