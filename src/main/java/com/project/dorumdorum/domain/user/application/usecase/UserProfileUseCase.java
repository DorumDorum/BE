package com.project.dorumdorum.domain.user.application.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.security.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.AuthErrorStatus.*;

@Service
@RequiredArgsConstructor
public class UserProfileUseCase {

    private final TokenProvider tokenProvider;
    private final UserProfileService userProfileService;

    public ProfileResponse me(HttpServletRequest request) {
        String accessToken = tokenProvider.getToken(request)
                .orElseThrow(() -> new RestApiException(EMPTY_JWT));

        Long userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));

        return userProfileService.getProfile(userNo);
    }

    public ProfileResponse getProfile(Long userNo) {
        return userProfileService.getProfile(userNo);
    }

    public ProfileResponse updateProfile(
            String authorization,
            UpdateProfileRequest body
    ) {
        String accessToken = tokenProvider.getToken(authorization)
                .orElseThrow(() -> new RestApiException(EMPTY_JWT));
        Long userNo = tokenProvider.getId(accessToken)
                .orElseThrow(() -> new RestApiException(INVALID_ID_TOKEN));

        return userProfileService.updateProfile(userNo, body);
    }
}
