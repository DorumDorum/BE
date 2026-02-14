package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.usecase.ConfirmRoomAssignmentUseCase;
import com.project.dorumdorum.domain.room.ui.ConfirmRoomAssignmentController;
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
@DisplayName("ConfirmRoomAssignmentController Unit Tests")
class ConfirmRoomAssignmentControllerTest {

    @Mock private ConfirmRoomAssignmentUseCase useCase;
    @InjectMocks private ConfirmRoomAssignmentController controller;

    @Test
    void confirm_CallsUseCase() {
        BaseResponse<Void> response = controller.confirm("u1", "r1");
        verify(useCase).execute("u1", "r1");
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
