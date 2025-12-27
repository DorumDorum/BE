package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.WriteNoticeUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.WriteNoticeApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WriteNoticeController implements WriteNoticeApiSpec {

    private final WriteNoticeUseCase writeNoticeUseCase;

    @Override
    public BaseResponse<NoticeResponse> writeNotice(
            @CurrentUser Long userNo,
            @RequestBody @Valid WriteNoticeRequest request
    ) {
        return BaseResponse.onSuccess(writeNoticeUseCase.execute(userNo, request));
    }
}
