package com.project.dorumdorum.domain.chat.unit.ui;

import com.project.dorumdorum.domain.chat.application.usecase.LeaveChatRoomUseCase;
import com.project.dorumdorum.domain.chat.ui.LeaveChatRoomController;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("LeaveChatRoomController Unit Tests")
class LeaveChatRoomControllerTest {

    @Mock private LeaveChatRoomUseCase useCase;
    @InjectMocks private LeaveChatRoomController controller;

    @Test
    @DisplayName("UseCase를 호출하고 성공 응답을 반환한다")
    void leave_CallsUseCaseAndReturnsSuccess() {
        ResponseEntity<BaseResponse<Void>> response = controller.leave("u1", "cr-1");

        verify(useCase).execute("cr-1", "u1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
