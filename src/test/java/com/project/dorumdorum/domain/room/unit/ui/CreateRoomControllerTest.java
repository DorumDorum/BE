package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.usecase.CreateRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.ui.CreateRoomController;
import com.project.dorumdorum.global.common.BaseResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateRoomController Unit Tests")
class CreateRoomControllerTest {

    @Mock private CreateRoomUseCase useCase;
    @InjectMocks private CreateRoomController controller;

    @Test
    void create_CallsUseCase() {
        RoomCreateRequest req = new RoomCreateRequest(RoomType.TYPE_1, 2, ResidencePeriod.SEMESTER, "title",
                mock(CreateRoomRuleRequest.class));
        BaseResponse<Void> response = controller.create("u1", req);

        verify(useCase).execute("u1", req);
        assertThat(response.getCode()).isEqualTo("COMMON200");
    }
}
