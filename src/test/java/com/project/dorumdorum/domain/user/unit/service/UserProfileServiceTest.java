package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.application.dto.request.UpdateProfileRequest;
import com.project.dorumdorum.domain.user.application.dto.response.ProfileResponse;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.domain.user.domain.service.UserProfileService;
import com.project.dorumdorum.domain.user.fixture.UserFixture;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileService Unit Tests")
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    @DisplayName("Should return profile when user exists")
    void getProfile_WithExistingUser_ReturnsProfile() {
        // Arrange
        String userNo = "0000000000000001";
        User user = UserFixture.createUserWithId(userNo);

        when(userRepository.findById(userNo)).thenReturn(Optional.of(user));

        // Act
        ProfileResponse response = userProfileService.getProfile(userNo);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.userNo()).isEqualTo(userNo);
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.email()).isEqualTo(user.getEmail());
        assertThat(response.nickname()).isEqualTo(user.getNickname());
        verify(userRepository).findById(userNo);
    }

    @Test
    @DisplayName("Should throw NotFoundException when user does not exist")
    void getProfile_WithNonExistentUser_ThrowsNotFoundException() {
        // Arrange
        String userNo = "nonexistent";

        when(userRepository.findById(userNo)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.getProfile(userNo))
                .isInstanceOf(RestApiException.class);

        verify(userRepository).findById(userNo);
    }

    @Test
    @DisplayName("Should update profile and return updated profile")
    void updateProfile_WithValidRequest_UpdatesAndReturnsProfile() {
        // Arrange
        String userNo = "0000000000000001";
        User user = UserFixture.createUserWithId(userNo);
        UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", "4", "정보보안학과");

        when(userRepository.findById(userNo)).thenReturn(Optional.of(user));

        // Act
        ProfileResponse response = userProfileService.updateProfile(userNo, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.userNo()).isEqualTo(userNo);
        assertThat(response.nickname()).isEqualTo(request.nickname());
        assertThat(response.grade()).isEqualTo(request.grade());
        assertThat(response.major()).isEqualTo(request.major());
        verify(userRepository).findById(userNo);
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent user")
    void updateProfile_WithNonExistentUser_ThrowsNotFoundException() {
        // Arrange
        String userNo = "nonexistent";
        UpdateProfileRequest request = new UpdateProfileRequest("새닉네임", "4", "정보보안학과");

        when(userRepository.findById(userNo)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userProfileService.updateProfile(userNo, request))
                .isInstanceOf(RestApiException.class);

        verify(userRepository).findById(userNo);
    }

    @Test
    @DisplayName("Should map user fields correctly to ProfileResponse")
    void getProfile_WithUser_MapsFieldsCorrectly() {
        // Arrange
        String userNo = "0000000000000001";
        User user = UserFixture.createDefaultUser();

        when(userRepository.findById(userNo)).thenReturn(Optional.of(user));

        // Act
        ProfileResponse response = userProfileService.getProfile(userNo);

        // Assert
        assertThat(response.userNo()).isEqualTo(user.getUserNo());
        assertThat(response.nickname()).isEqualTo(user.getNickname());
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.email()).isEqualTo(user.getEmail());
        assertThat(response.gender()).isEqualTo(user.getGender());
        assertThat(response.studentNo()).isEqualTo(user.getStudentNo());
        assertThat(response.major()).isEqualTo(user.getMajor());
        assertThat(response.grade()).isEqualTo(user.getGrade());
        assertThat(response.birth()).isEqualTo(user.getBirth());
        assertThat(response.age()).isEqualTo(user.getAge());
    }
}
