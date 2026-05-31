package com.project.dorumdorum.domain.user.unit.usecase;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.usecase.SignUpUseCase;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.EmailVerifiedRepository;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import com.project.dorumdorum.domain.user.fixture.RequestFixture;
import com.project.dorumdorum.domain.user.fixture.UserFixture;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.logging.DomainEventLogger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SignUpUseCase Unit Tests")
class SignUpUseCaseTest {

    @Mock
    private UserService userService;
    @Mock
    private EmailVerifiedRepository emailVerifiedRepository;
    @Mock
    private DomainEventLogger domainEventLogger;

    @InjectMocks
    private SignUpUseCase signUpUseCase;

    @Test
    @DisplayName("Should save user successfully with valid request")
    void execute_WithValidRequest_SavesUser() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();
        User savedUser = UserFixture.createDefaultUser();

        when(emailVerifiedRepository.existsByEmail(request.email())).thenReturn(true);
        when(userService.isAlreadyRegistered(request.email())).thenReturn(false);
        when(userService.save(request)).thenReturn(savedUser);

        // Act & Assert
        assertThatCode(() -> signUpUseCase.execute(request))
                .doesNotThrowAnyException();

        verify(userService).isAlreadyRegistered(request.email());
        verify(userService).save(request);
    }

    @Test
    @DisplayName("Should throw PasswordNotMatchesException when passwords do not match")
    void execute_WithPasswordMismatch_ThrowsPasswordNotMatchesException() {
        // Arrange
        SignUpRequest request = RequestFixture.createSignUpRequestWithMismatchedPassword();

        // Act & Assert
        assertThatThrownBy(() -> signUpUseCase.execute(request))
                .isInstanceOf(RestApiException.class);

        verify(userService, never()).isAlreadyRegistered(anyString());
        verify(userService, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AlreadyRegisteredEmailException when email is already registered")
    void execute_WithAlreadyRegisteredEmail_ThrowsAlreadyRegisteredEmailException() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();
        when(emailVerifiedRepository.existsByEmail(request.email())).thenReturn(true);
        when(userService.isAlreadyRegistered(request.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> signUpUseCase.execute(request))
                .isInstanceOf(RestApiException.class);

        verify(userService).isAlreadyRegistered(request.email());
        verify(userService, never()).save(any());
    }

    @Test
    @DisplayName("Should call UserService methods in correct order")
    void execute_WithValidRequest_CallsUserServiceInCorrectOrder() {
        // Arrange
        SignUpRequest request = RequestFixture.createValidSignUpRequest();
        User savedUser = UserFixture.createDefaultUser();

        when(emailVerifiedRepository.existsByEmail(request.email())).thenReturn(true);
        when(userService.isAlreadyRegistered(request.email())).thenReturn(false);
        when(userService.save(request)).thenReturn(savedUser);

        // Act
        signUpUseCase.execute(request);

        // Assert
        InOrder inOrder = inOrder(userService);
        inOrder.verify(userService).isAlreadyRegistered(request.email());
        inOrder.verify(userService).save(request);
    }
}
