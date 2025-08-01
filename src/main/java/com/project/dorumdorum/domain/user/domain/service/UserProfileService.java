package com.project.dorumdorum.domain.user.domain.service;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile(Long userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        return ProfileResponse.create(user);
    }

    public ProfileResponse updateProfile(Long userNo, UpdateProfileRequest updateProfileRequest) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        user.updateProfile(updateProfileRequest);

        return ProfileResponse.create(user);
    }
}
