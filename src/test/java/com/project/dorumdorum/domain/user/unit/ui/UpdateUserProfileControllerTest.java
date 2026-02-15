package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.UpdateUserProfileUseCase;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.ui.UpdateUserProfileController;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateUserProfileController Unit Tests")
class UpdateUserProfileControllerTest {

    @Mock
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @InjectMocks
    private UpdateUserProfileController controller;

    @Test
    @DisplayName("Should update profile and return updated result")
    void updateProfile_ReturnsUpdatedProfile() {
        String userNo = "0000000000000001";
        UpdateProfileRequest request = new UpdateProfileRequest("nickname", "3", "major");
        ProfileResponse expected = new ProfileResponse(
                userNo, "nickname", "name", "email@univ.ac.kr", Gender.MALE,
                "20210001", "major", "3", "2000-01-01", 25
        );
        when(updateUserProfileUseCase.execute(userNo, request)).thenReturn(expected);

        ResponseEntity<ProfileResponse> response = controller.updateProfile(userNo, request);

        verify(updateUserProfileUseCase).execute(userNo, request);
        assertThat(response.getBody()).isEqualTo(expected);
    }
}
