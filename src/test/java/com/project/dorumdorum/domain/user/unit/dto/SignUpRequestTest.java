package com.project.dorumdorum.domain.user.unit.dto;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.fixture.RequestFixture;
import com.project.dorumdorum.global.exception.RestApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SignUpRequest DTO Tests")
class SignUpRequestTest {

    @Test
    @DisplayName("Should return true when passwords match")
    void isCheckedPassword_WithMatchingPasswords_ReturnsTrue() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();

        // Act
        boolean result = request.isCheckedPassword();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when passwords do not match")
    void isCheckedPassword_WithNonMatchingPasswords_ReturnsFalse() {
        // Arrange
        SignUpRequest request = RequestFixture.createSignUpRequestWithMismatchedPassword();

        // Act
        boolean result = request.isCheckedPassword();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should calculate age correctly")
    void calculateAge_WithValidBirthDate_ReturnsCorrectAge() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();

        // Act
        int age = request.calculateAge();

        // Assert
        assertThat(age).isGreaterThan(0);
        assertThat(age).isLessThan(150); // Reasonable age range
    }

    @Test
    @DisplayName("Should throw exception with invalid birth date format")
    void calculateAge_WithInvalidBirthDate_ThrowsException() {
        // Arrange
        SignUpRequest request = RequestFixture.createSignUpRequestWithInvalidBirth();

        // Act & Assert
        assertThatThrownBy(() -> request.calculateAge())
                .isInstanceOf(RestApiException.class);
    }
}
