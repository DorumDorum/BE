package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.response.RoomRequestApplicationResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadRoomApplicationsUseCase;
import com.project.dorumdorum.domain.room.ui.LoadRoomApplicationsController;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadRoomApplicationsController Unit Tests")
class LoadRoomApplicationsControllerTest {

    @Mock private LoadRoomApplicationsUseCase useCase;
    @InjectMocks private LoadRoomApplicationsController controller;

    @Test
    @DisplayName("Should return applications loaded by use case")
    void loadApplications_ReturnsUseCaseResult() {
        List<RoomRequestApplicationResponse> payload = List.of(
                RoomRequestApplicationResponse.builder().requestNo("rq1").userNo("u2").build()
        );
        when(useCase.execute("u1", "r1")).thenReturn(payload);

        ResponseEntity<List<RoomRequestApplicationResponse>> response = controller.loadApplications("u1", "r1");

        verify(useCase).execute("u1", "r1");
        assertThat(response.getBody()).isEqualTo(payload);
    }
}
