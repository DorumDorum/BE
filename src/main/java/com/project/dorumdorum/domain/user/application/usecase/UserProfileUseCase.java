package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileUseCase {

    private final UserProfileService userProfileService;

    public ProfileResponse me(Long userNo) {
        return userProfileService.getProfile(userNo);
    }

    public ProfileResponse getProfile(Long userNo) {
        return userProfileService.getProfile(userNo);
    }

    @Transactional
    public ProfileResponse updateProfile(Long userNo, UpdateProfileRequest updateProfileRequest) {
        return userProfileService.updateProfile(userNo, updateProfileRequest);
    }
}
