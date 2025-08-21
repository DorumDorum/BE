package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.UpdateUserProfileUseCase;
import com.project.dorumdorum.domain.user.ui.spec.UpdateUserProfileApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateUserProfileController implements UpdateUserProfileApiSpec {

    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @Override
    public BaseResponse<ProfileResponse> updateProfile(
            @CurrentUser Long userNo,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        return BaseResponse.onSuccess(updateUserProfileUseCase.execute(userNo, request));
    }
}
