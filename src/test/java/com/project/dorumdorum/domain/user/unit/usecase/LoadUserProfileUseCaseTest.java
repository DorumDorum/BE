package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoadUserProfileUseCase;
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
@DisplayName("LoadUserProfileUseCase Unit Tests")
class LoadUserProfileUseCaseTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private LoadUserProfileUseCase loadUserProfileUseCase;

    @Test
    @DisplayName("Should return profile from UserProfileService")
    void execute_ReturnsProfileFromService() {
        // Arrange
        String userNo = "0000000000000001";
        ProfileResponse response = new ProfileResponse(
                userNo, "nickname", "name", "email@univ.ac.kr", Gender.MALE,
                "20210001", "major", "grade", "2000-01-01", 25
        );
        when(userProfileService.getProfile(userNo)).thenReturn(response);

        // Act
        ProfileResponse result = loadUserProfileUseCase.execute(userNo);

        // Assert
        assertThat(result).isEqualTo(response);
        verify(userProfileService).getProfile(userNo);
    }
}
