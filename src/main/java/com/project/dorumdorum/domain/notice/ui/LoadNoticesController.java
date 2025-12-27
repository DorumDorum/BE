package com.project.dorumdorum.domain.notice.ui;


import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticesResponse;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticesUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.LoadNoticesApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadNoticesController implements LoadNoticesApiSpec {

    private final LoadNoticesUseCase loadNoticesUseCase;

    @Override
    public BaseResponse<List<NoticesResponse>> loadNotices(
            @CurrentUser Long userNo,
            @RequestParam Long roomNo
    ) {
        return BaseResponse.onSuccess(loadNoticesUseCase.loadNotices(userNo, roomNo));
    }

    @Override
    public BaseResponse<NoticeResponse> loadNotice(
            @CurrentUser Long userNo,
            @RequestParam Long noticeNo
    ) {
        return BaseResponse.onSuccess(loadNoticesUseCase.loadNotice(userNo, noticeNo));
    }

}
