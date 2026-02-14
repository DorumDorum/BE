package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.usecase.ApplyRoomUseCase;
import com.project.dorumdorum.domain.room.ui.ApplyRoomController;
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
@DisplayName("ApplyRoomController Unit Tests")
class ApplyRoomControllerTest {

    @Mock private ApplyRoomUseCase useCase;
    @InjectMocks private ApplyRoomController controller;

    @Test
    void join_CallsUseCase() {
        JoinRoomRequest req = new JoinRoomRequest("intro", "msg");
        BaseResponse<Void> response = controller.join("u1", "r1", req);
        verify(useCase).execute("u1", "r1", req);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
