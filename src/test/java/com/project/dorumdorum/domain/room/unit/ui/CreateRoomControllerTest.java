package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.checklist.application.dto.request.CreateRoomRuleRequest;
import com.project.dorumdorum.domain.room.application.dto.request.RoomCreateRequest;
import com.project.dorumdorum.domain.room.application.usecase.CreateRoomUseCase;
import com.project.dorumdorum.domain.room.domain.entity.ResidencePeriod;
import com.project.dorumdorum.domain.room.domain.entity.RoomType;
import com.project.dorumdorum.domain.room.ui.CreateRoomController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateRoomController Unit Tests")
class CreateRoomControllerTest {

    @Mock private CreateRoomUseCase useCase;
    @InjectMocks private CreateRoomController controller;

    @Test
    void create_CallsUseCase() {
        RoomCreateRequest req = new RoomCreateRequest(RoomType.TYPE_1, 2, ResidencePeriod.SEMESTER, "title", null,
                mock(CreateRoomRuleRequest.class));
        when(useCase.execute("u1", req)).thenReturn("r1");
        ResponseEntity<Void> response = controller.create("u1", req);

        verify(useCase).execute("u1", req);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/rooms/r1"));
        assertThat(response.getBody()).isNull();
    }
}
