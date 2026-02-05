package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoadUserProfileUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoadUserProfileApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoadUserProfileController implements LoadUserProfileApiSpec {

    private final LoadUserProfileUseCase loadUserProfileUseCase;

    @Override
    public BaseResponse<ProfileResponse> loadMyProfile(
            @CurrentUser String userNo
    ) {
        return BaseResponse.onSuccess(loadUserProfileUseCase.execute(userNo));
    }

    @Override
    public BaseResponse<ProfileResponse> loadProfile(
            @PathVariable("userNo") String userNo
    ) {
        return BaseResponse.onSuccess(loadUserProfileUseCase.execute(userNo));
    }
}
