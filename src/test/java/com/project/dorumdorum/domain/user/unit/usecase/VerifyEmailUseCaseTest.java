package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.usecase.VerifyEmailUseCase;
import com.project.dorumdorum.domain.user.domain.service.EmailVerificationService;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("VerifyEmailUseCase Unit Tests")
class VerifyEmailUseCaseTest {

    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private DomainEventLogger domainEventLogger;

    @InjectMocks
    private VerifyEmailUseCase verifyEmailUseCase;

    @Test
    @DisplayName("Should delegate verification to EmailVerificationService")
    void execute_DelegatesToEmailVerificationService() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        // Act
        verifyEmailUseCase.execute(email, code);

        // Assert
        verify(emailVerificationService).verifyCode(email, code);
    }
}
