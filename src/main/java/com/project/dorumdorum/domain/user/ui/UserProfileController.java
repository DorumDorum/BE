package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.UserProfileUseCase;
import com.project.dorumdorum.global.common.BaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/profile")
public class UserProfileController {

    private final UserProfileUseCase userProfileUseCase;

    @GetMapping("/me")
    public BaseResponse<ProfileResponse> me(HttpServletRequest request) {
        return BaseResponse.onSuccess(userProfileUseCase.me(request));
    }

    @GetMapping("{userNo}")
    public BaseResponse<ProfileResponse> getProfile(@PathVariable("userNo") Long userNo) {
        return BaseResponse.onSuccess(userProfileUseCase.getProfile(userNo));
    }
}
