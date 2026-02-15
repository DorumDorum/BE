package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.usecase.SendVerificationEmailUseCase;
import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import com.project.dorumdorum.global.util.SecureRandomGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendVerificationEmailUseCase Unit Tests")
class SendVerificationEmailUseCaseTest {

    @Mock
    private UserService userService;

    @Mock
    private EmailVerificationService emailVerificationService;

    @Mock
    private SecureRandomGenerator secureRandomGenerator;
    @Mock
    private DomainEventLogger domainEventLogger;

    @InjectMocks
    private SendVerificationEmailUseCase sendVerificationEmailUseCase;

    @Test
    @DisplayName("Should send verification email with valid unregistered email")
    void send_WithValidUnregisteredEmail_SendsVerificationCode() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        when(userService.isAlreadyRegistered(email)).thenReturn(false);
        when(emailVerificationService.isAllowedUniversityEmail(email)).thenReturn(true);
        when(secureRandomGenerator.generate()).thenReturn(code);

        // Act & Assert
        assertThatCode(() -> sendVerificationEmailUseCase.send(email))
                .doesNotThrowAnyException();

        verify(userService).isAlreadyRegistered(email);
        verify(emailVerificationService).isAllowedUniversityEmail(email);
        verify(secureRandomGenerator).generate();
        verify(emailVerificationService).sendCode(email, code);
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException with already registered email")
    void send_WithAlreadyRegisteredEmail_ThrowsDuplicateEmailException() {
        // Arrange
        String email = "test@university.ac.kr";

        when(userService.isAlreadyRegistered(email)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> sendVerificationEmailUseCase.send(email))
                .isInstanceOf(RestApiException.class);

        verify(userService).isAlreadyRegistered(email);
        verify(emailVerificationService, never()).isAllowedUniversityEmail(anyString());
        verify(secureRandomGenerator, never()).generate();
        verify(emailVerificationService, never()).sendCode(anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw InvalidEmailDomainException with invalid domain")
    void send_WithInvalidDomain_ThrowsInvalidEmailDomainException() {
        // Arrange
        String email = "test@gmail.com";

        when(userService.isAlreadyRegistered(email)).thenReturn(false);
        when(emailVerificationService.isAllowedUniversityEmail(email)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> sendVerificationEmailUseCase.send(email))
                .isInstanceOf(RestApiException.class);

        verify(userService).isAlreadyRegistered(email);
        verify(emailVerificationService).isAllowedUniversityEmail(email);
        verify(secureRandomGenerator, never()).generate();
        verify(emailVerificationService, never()).sendCode(anyString(), anyString());
    }

    @Test
    @DisplayName("Should generate and send verification code")
    void send_WithValidEmail_GeneratesAndSendsCode() {
        // Arrange
        String email = "test@university.ac.kr";
        String generatedCode = "987654";

        when(userService.isAlreadyRegistered(email)).thenReturn(false);
        when(emailVerificationService.isAllowedUniversityEmail(email)).thenReturn(true);
        when(secureRandomGenerator.generate()).thenReturn(generatedCode);

        // Act
        sendVerificationEmailUseCase.send(email);

        // Assert
        verify(emailVerificationService).sendCode(email, generatedCode);
    }
}
