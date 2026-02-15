package com.project.dorumdorum.domain.user.ui;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoadUserProfileUseCase;
import com.project.dorumdorum.domain.user.ui.spec.LoadUserProfileApiSpec;
import com.project.dorumdorum.global.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoadUserProfileController implements LoadUserProfileApiSpec {

    private final LoadUserProfileUseCase loadUserProfileUseCase;

    @Override
    public ResponseEntity<ProfileResponse> loadMyProfile(
            @CurrentUser String userNo
    ) {
        return ResponseEntity.ok(loadUserProfileUseCase.execute(userNo));
    }

    @Override
    public ResponseEntity<ProfileResponse> loadProfile(
            @PathVariable("userNo") String userNo
    ) {
        return ResponseEntity.ok(loadUserProfileUseCase.execute(userNo));
    }
}
