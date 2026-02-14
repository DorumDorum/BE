package com.project.dorumdorum.domain.user.unit.ui;

import com.project.dorumdorum.domain.user.application.dto.request.SignUpRequest;
import com.project.dorumdorum.domain.user.application.usecase.SignUpUseCase;
import com.project.dorumdorum.domain.user.domain.entity.Gender;
import com.project.dorumdorum.domain.user.ui.SignUpController;
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
@DisplayName("SignUpController Unit Tests")
class SignUpControllerTest {

    @Mock
    private SignUpUseCase signUpUseCase;

    @InjectMocks
    private SignUpController controller;

    @Test
    @DisplayName("Should call use case and return success")
    void signUp_CallsUseCaseAndReturnsSuccess() {
        SignUpRequest request = new SignUpRequest(
                "name", "nickname", "test@university.ac.kr",
                "password123!", "password123!", Gender.MALE,
                "20210001", "major", "3", "2000-01-01"
        );

        BaseResponse<Void> response = controller.signUp(request);

        verify(signUpUseCase).execute(request);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
