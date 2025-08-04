package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.UserProfileUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class UserProfileController {

    private final UserProfileUseCase userProfileUseCase;

    @GetMapping("/me")
    public BaseResponse<ProfileResponse> getMyProfile(@CurrentUser Long userNo) {
        return BaseResponse.onSuccess(userProfileUseCase.getMyProfile(userNo));
    }

    @GetMapping("/{userNo}")
    public BaseResponse<ProfileResponse> getProfile(@PathVariable("userNo") Long userNo) {
        return BaseResponse.onSuccess(userProfileUseCase.getProfile(userNo));
    }

    @PatchMapping("")
    public BaseResponse<ProfileResponse> updateProfile(@CurrentUser Long userNo, @RequestBody @Valid UpdateProfileRequest body) {
        return BaseResponse.onSuccess(userProfileUseCase.updateProfile(userNo, body));
    }
}
