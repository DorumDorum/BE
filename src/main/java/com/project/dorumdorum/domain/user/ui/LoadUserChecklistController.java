package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.UserChecklistResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoadUserChecklistUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoadUserChecklistApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadUserChecklistController implements LoadUserChecklistApiSpec {

    private final LoadUserChecklistUseCase loadUserChecklistUseCase;

    @Override
    public BaseResponse<UserChecklistResponse> loadMyChecklist(
            @CurrentUser String userNo
    ) {
        return BaseResponse.onSuccess(loadUserChecklistUseCase.execute(userNo));
    }

    @Override
    public BaseResponse<UserChecklistResponse> loadUserChecklist(
            @PathVariable String userNo
    ) {
        return BaseResponse.onSuccess(loadUserChecklistUseCase.execute(userNo));
    }
}
