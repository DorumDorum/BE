package com.project.dorumdorum.domain.notice.ui;


import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticesUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadNoticeController {

    private final LoadNoticesUseCase loadNoticesUseCase;

    @GetMapping("/api/notices")
    public BaseResponse<List<NoticeResponse>> loadNotices(
            @CurrentUser Long userNo,
            @RequestParam Long roomNo
    ) {
        return BaseResponse.onSuccess(loadNoticesUseCase.execute(userNo, roomNo));
    }

}
