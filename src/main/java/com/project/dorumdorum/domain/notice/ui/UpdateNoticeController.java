package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.UpdateNoticeUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.UpdateNoticeApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateNoticeController implements UpdateNoticeApiSpec {

    private final UpdateNoticeUseCase updateNoticeUseCase;

    @Override
    public BaseResponse<NoticeResponse> updateNotice(
            @CurrentUser Long userNo,
            @RequestBody UpdateNoticeRequest request
    ) {
        return BaseResponse.onSuccess(updateNoticeUseCase.execute(userNo, request));
    }
}
