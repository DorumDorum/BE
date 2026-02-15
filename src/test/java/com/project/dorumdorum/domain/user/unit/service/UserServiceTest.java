package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.domain.entity.Role;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.domain.user.fixture.RequestFixture;
import com.project.dorumdorum.domain.user.fixture.UserFixture;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return user when email exists")
    void findByEmail_WithExistingEmail_ReturnsUser() {
        // Arrange
        String email = "test@university.ac.kr";
        User expectedUser = UserFixture.createUserWithEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findByEmail(email);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw EmailNotFoundException when email does not exist")
    void findByEmail_WithNonExistentEmail_ThrowsEmailNotFoundException() {
        // Arrange
        String email = "nonexistent@university.ac.kr";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findByEmail(email))
                .isInstanceOf(RestApiException.class);
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should return true when email is already registered")
    void isAlreadyRegistered_WithExistingEmail_ReturnsTrue() {
        // Arrange
        String email = "test@university.ac.kr";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userService.isAlreadyRegistered(email);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("Should return false when email is not registered")
    void isAlreadyRegistered_WithNonExistentEmail_ReturnsFalse() {
        // Arrange
        String email = "nonexistent@university.ac.kr";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userService.isAlreadyRegistered(email);

        // Assert
        assertThat(result).isFalse();
        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("Should encode password and save user")
    void save_WithValidRequest_EncodesPasswordAndSavesUser() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();
        String encodedPassword = "encodedPassword123!";
        User savedUser = UserFixture.createDefaultUser();

        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.save(request);

        // Assert
        assertThat(result).isNotNull();
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(request.email());
        assertThat(capturedUser.getName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("Should set role to USER when saving user")
    void save_WithValidRequest_SetsRoleToUser() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();
        User savedUser = UserFixture.createDefaultUser();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        userService.save(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Should calculate age correctly when saving user")
    void save_WithValidRequest_CalculatesAgeCorrectly() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();
        User savedUser = UserFixture.createDefaultUser();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        userService.save(request);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getAge()).isNotNull();
        assertThat(capturedUser.getAge()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Should return user when id exists")
    void findById_WithExistingId_ReturnsUser() {
        // Arrange
        String userNo = "0000000000000001";
        User expectedUser = UserFixture.createUserWithId(userNo);
        when(userRepository.findById(userNo)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.findById(userNo);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserNo()).isEqualTo(userNo);
        verify(userRepository).findById(userNo);
    }

    @Test
    @DisplayName("Should throw NotFoundException when id does not exist")
    void findById_WithNonExistentId_ThrowsNotFoundException() {
        // Arrange
        String userNo = "nonexistent";
        when(userRepository.findById(userNo)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findById(userNo))
                .isInstanceOf(RestApiException.class);
        verify(userRepository).findById(userNo);
    }

    @Test
    @DisplayName("Should not throw exception when id exists")
    void validateExistsById_WithExistingId_DoesNotThrow() {
        // Arrange
        String userNo = "0000000000000001";
        when(userRepository.existsById(userNo)).thenReturn(true);

        // Act & Assert
        assertThatCode(() -> userService.validateExistsById(userNo))
                .doesNotThrowAnyException();
        verify(userRepository).existsById(userNo);
    }

    @Test
    @DisplayName("Should throw NotFoundException when id does not exist during validation")
    void validateExistsById_WithNonExistentId_ThrowsNotFoundException() {
        // Arrange
        String userNo = "nonexistent";
        when(userRepository.existsById(userNo)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.validateExistsById(userNo))
                .isInstanceOf(RestApiException.class);
        verify(userRepository).existsById(userNo);
    }
}
