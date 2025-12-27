package com.project.dorumdorum.domain.notice.ui;


import com.project.dorumdorum.domain.notice.application.usecase.DeleteNoticeUseCase;
import com.project.dorumdorum.domain.notice.ui.spec.DeleteNoticeApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeleteNoticeController implements DeleteNoticeApiSpec {

    private final DeleteNoticeUseCase deleteNoticeUseCase;

    @Override
    public BaseResponse<Void> deleteNotice(
            @CurrentUser Long userNo,
            @RequestParam Long noticeNo
    ) {
        deleteNoticeUseCase.execute(userNo, noticeNo);
        return BaseResponse.onSuccess();
    }
}
