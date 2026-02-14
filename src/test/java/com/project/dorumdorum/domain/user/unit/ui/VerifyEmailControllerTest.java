package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.usecase.VerifyEmailUseCase;
import com.project.dorumdorum.domain.user.ui.VerifyEmailController;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("VerifyEmailController Unit Tests")
class VerifyEmailControllerTest {

    @Mock
    private VerifyEmailUseCase verifyEmailUseCase;

    @InjectMocks
    private VerifyEmailController verifyEmailController;

    @Test
    @DisplayName("Should call use case and return success response")
    void verifyEmail_CallsUseCaseAndReturnsSuccess() {
        // Arrange
        String email = "test@university.ac.kr";
        String code = "123456";

        // Act
        BaseResponse<Void> response = verifyEmailController.verifyEmail(email, code);

        // Assert
        verify(verifyEmailUseCase).execute(email, code);
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("COMMON200");
        assertThat(response.getMessage()).isEqualTo("요청에 성공하였습니다.");
    }
}
