package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.UpdateUserProfileUseCase;
import com.project.dorumdorum.domain.user.ui.spec.UpdateUserProfileApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateUserProfileController implements UpdateUserProfileApiSpec {

    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @Override
    public ResponseEntity<ProfileResponse> updateProfile(
            @CurrentUser String userNo,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(updateUserProfileUseCase.execute(userNo, request));
    }
}
