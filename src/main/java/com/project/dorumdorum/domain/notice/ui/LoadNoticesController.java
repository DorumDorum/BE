package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticesUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.LoadNoticesApiSpec;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadNoticesController implements LoadNoticesApiSpec {

    private final LoadNoticesUseCase loadNoticesUseCase;

    @Override
    public ResponseEntity<List<NoticeResponse>> loadNotices() {
        List<NoticeResponse> notices = loadNoticesUseCase.execute();
        return ResponseEntity.ok(notices);
    }
}
