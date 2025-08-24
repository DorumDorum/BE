package com.project.dorumdorum.domain.notice.ui;

import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.WriteNoticeResponse;
import com.project.dorumdorum.domain.notice.application.usecase.WriteNoticeUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WriteNoticeController {

    private final WriteNoticeUseCase writeNoticeUseCase;

    @PostMapping("/api/notice")
    public BaseResponse<WriteNoticeResponse> writeNotice(
            @CurrentUser Long userNo,
            @RequestBody @Valid WriteNoticeRequest request
    ) {
        return BaseResponse.onSuccess(writeNoticeUseCase.execute(userNo, request));
    }
}
