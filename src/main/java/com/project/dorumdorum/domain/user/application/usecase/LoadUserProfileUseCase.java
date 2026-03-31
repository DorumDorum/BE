package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserProfileUseCase {

    private final UserProfileService userProfileService;

    /**
     * 사용자 프로필 조회
     * - 사용자 프로필 정보를 조회
     * - 화면 표시용 응답으로 반환
     */
    public ProfileResponse execute(String userNo) {
        return userProfileService.getProfile(userNo);
    }
}
