package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.MyUserChecklistResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoadMyUserChecklistUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoadMyUserChecklistApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadMyUserChecklistController implements LoadMyUserChecklistApiSpec {

    private final LoadMyUserChecklistUseCase loadMyUserChecklistUseCase;

    @Override
    public BaseResponse<MyUserChecklistResponse> load(
            @CurrentUser Long userNo
    ) {
        return BaseResponse.onSuccess(loadMyUserChecklistUseCase.execute(userNo));
    }
}
