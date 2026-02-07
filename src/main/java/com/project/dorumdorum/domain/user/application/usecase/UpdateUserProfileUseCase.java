package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserProfileService userProfileService;

    public ProfileResponse execute(String userNo, UpdateProfileRequest updateProfileRequest) {
        return userProfileService.updateProfile(userNo, updateProfileRequest);
    }
}
