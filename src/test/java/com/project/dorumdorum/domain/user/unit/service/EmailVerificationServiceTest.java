package com.project.dorumdorum.domain.user.unit.service;

import com.project.dorumdorum.domain.user.domain.repository.VerificationCodeRepository;
import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.domain.user.infra.smtp.SmtpEmailSender;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import com.project.dorumdorum.global.properties.IncludeEmailDomainProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailVerificationService Unit Tests")
class EmailVerificationServiceTest {

    @Mock
    private IncludeEmailDomainProperties includeEmailDomainProperties;

    @Mock
    private VerificationCodeRepository verificationCodeRepository;

    @Mock
    private SmtpEmailSender smtpEmailSender;
    @Mock
    private DomainEventLogger domainEventLogger;

    @InjectMocks
    private EmailVerificationService emailVerificationService;

    @Test
    @DisplayName("Should return true for allowed university email domain")
    void isAllowedUniversityEmail_WithAllowedDomain_ReturnsTrue() {
        // Arrange
        String email = "test@university.ac.kr";
        String domain = "university.ac.kr";

        when(includeEmailDomainProperties.matches(domain)).thenReturn(true);

        // Act
        boolean result = emailVerificationService.isAllowedUniversityEmail(email);

        // Assert
        assertThat(result).isTrue();
        verify(includeEmailDomainProperties).matches(domain);
    }

    @Test
    @DisplayName("Should return false for non-allowed email domain")
    void isAllowedUniversityEmail_WithNonAllowedDomain_ReturnsFalse() {
        // Arrange
        String email = "test@gmail.com";
        String domain = "gmail.com";

        when(includeEmailDomainProperties.matches(domain)).thenReturn(false);

        // Act
        boolean result = emailVerificationService.isAllowedUniversityEmail(email);

        // Assert
        assertThat(result).isFalse();
        verify(includeEmailDomainProperties).matches(domain);
    }

    @Test
    @DisplayName("Should save code and send email")
    void sendCode_WithValidEmailAndCode_SavesCodeAndSendsEmail() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        // Act
        emailVerificationService.sendCode(email, code);

        // Assert
        verify(verificationCodeRepository).save(email, code);
        verify(smtpEmailSender).send(eq(email), anyString(), anyString());
    }

    @Test
    @DisplayName("Should verify code successfully with matching code")
    void verifyCode_WithMatchingCode_SuccessfullyVerifies() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(code));

        // Act & Assert
        assertThatCode(() -> emailVerificationService.verifyCode(email, code))
                .doesNotThrowAnyException();

        verify(verificationCodeRepository).findByEmail(email);
        verify(verificationCodeRepository).delete(email);
    }

    @Test
    @DisplayName("Should throw exception with non-matching code")
    void verifyCode_WithNonMatchingCode_ThrowsException() {
        // Arrange
        String email = "test@university.ac.kr";
        String correctCode = "123456";
        String wrongCode = "654321";

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(correctCode));

        // Act & Assert
        assertThatThrownBy(() -> emailVerificationService.verifyCode(email, wrongCode))
                .isInstanceOf(RestApiException.class);

        verify(verificationCodeRepository).findByEmail(email);
        verify(verificationCodeRepository, never()).delete(anyString());
    }

    @Test
    @DisplayName("Should throw exception when code not found")
    void verifyCode_WithCodeNotFound_ThrowsException() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> emailVerificationService.verifyCode(email, code))
                .isInstanceOf(RestApiException.class);

        verify(verificationCodeRepository).findByEmail(email);
        verify(verificationCodeRepository, never()).delete(anyString());
    }

    @Test
    @DisplayName("Should delete code after successful verification")
    void verifyCode_AfterSuccessfulVerification_DeletesCode() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        when(verificationCodeRepository.findByEmail(email)).thenReturn(Optional.of(code));

        // Act
        emailVerificationService.verifyCode(email, code);

        // Assert
        verify(verificationCodeRepository).delete(email);
    }
}
