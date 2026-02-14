package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.UpdateUserProfileUseCase;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
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
@DisplayName("UpdateUserProfileUseCase Unit Tests")
class UpdateUserProfileUseCaseTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @Test
    @DisplayName("Should update profile through UserProfileService")
    void execute_UpdatesProfileThroughService() {
        // Arrange
        String userNo = "0000000000000001";
        UpdateProfileRequest request = new UpdateProfileRequest("nickname", "major", "grade");
        ProfileResponse response = new ProfileResponse(
                userNo, "nickname", "name", "email@univ.ac.kr", Gender.FEMALE,
                "20210001", "major", "grade", "2000-01-01", 25
        );
        when(userProfileService.updateProfile(userNo, request)).thenReturn(response);

        // Act
        ProfileResponse result = updateUserProfileUseCase.execute(userNo, request);

        // Assert
        assertThat(result).isEqualTo(response);
        verify(userProfileService).updateProfile(userNo, request);
    }
}
