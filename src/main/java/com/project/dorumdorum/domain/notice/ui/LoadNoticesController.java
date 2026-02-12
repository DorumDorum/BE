package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.LoadNoticesUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.LoadNoticesApiSpec;
import com.project.dorumdorum.global.annotation.AccessToken;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadNoticesController implements LoadNoticesApiSpec {

    private final LoadNoticesUseCase loadNoticesUseCase;

    @Override
    public BaseResponse<List<NoticeResponse>> loadNotices(
            @AccessToken String userNo
    ) {
        List<NoticeResponse> notices = loadNoticesUseCase.execute();
        return BaseResponse.onSuccess(notices);
    }
}
