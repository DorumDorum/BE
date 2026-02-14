package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.response.CheckMyRoomResponse;
import com.project.dorumdorum.domain.room.application.usecase.CheckMyRoomUseCase;
import com.project.dorumdorum.domain.room.ui.CheckMyRoomController;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CheckMyRoomController Unit Tests")
class CheckMyRoomControllerTest {

    @Mock private CheckMyRoomUseCase useCase;
    @InjectMocks private CheckMyRoomController controller;

    @Test
    void check_ReturnsUseCaseResponse() {
        CheckMyRoomResponse payload = new CheckMyRoomResponse(true, "r1");
        when(useCase.execute("u1")).thenReturn(payload);

        BaseResponse<CheckMyRoomResponse> response = controller.check("u1");

        verify(useCase).execute("u1");
        assertThat(response.getResult()).isEqualTo(payload);
    }
}
