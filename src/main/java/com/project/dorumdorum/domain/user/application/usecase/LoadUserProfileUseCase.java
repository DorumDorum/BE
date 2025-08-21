package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserProfileUseCase {

    private final UserProfileService userProfileService;

    public ProfileResponse execute(Long userNo) {
        return userProfileService.getProfile(userNo);
    }
}
