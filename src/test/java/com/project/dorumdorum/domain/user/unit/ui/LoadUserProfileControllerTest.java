package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.application.usecase.LoadUserProfileUseCase;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.ui.LoadUserProfileController;
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
@DisplayName("LoadUserProfileController Unit Tests")
class LoadUserProfileControllerTest {

    @Mock
    private LoadUserProfileUseCase loadUserProfileUseCase;

    @InjectMocks
    private LoadUserProfileController controller;

    @Test
    @DisplayName("Should load my profile using current user id")
    void loadMyProfile_ReturnsProfile() {
        String userNo = "0000000000000001";
        ProfileResponse expected = new ProfileResponse(
                userNo, "nickname", "name", "email@univ.ac.kr", Gender.MALE,
                "20210001", "major", "3", "2000-01-01", 25
        );
        when(loadUserProfileUseCase.execute(userNo)).thenReturn(expected);

        ResponseEntity<ProfileResponse> response = controller.loadMyProfile(userNo);

        verify(loadUserProfileUseCase).execute(userNo);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should load profile by path user id")
    void loadProfile_ReturnsProfile() {
        String userNo = "0000000000000002";
        ProfileResponse expected = new ProfileResponse(
                userNo, "nick2", "name2", "email2@univ.ac.kr", Gender.FEMALE,
                "20210002", "major2", "4", "1999-01-01", 26
        );
        when(loadUserProfileUseCase.execute(userNo)).thenReturn(expected);

        ResponseEntity<ProfileResponse> response = controller.loadProfile(userNo);

        verify(loadUserProfileUseCase).execute(userNo);
        assertThat(response.getBody()).isEqualTo(expected);
    }
}
