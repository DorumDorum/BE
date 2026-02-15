package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.request.JoinRoomRequest;
import com.project.dorumdorum.domain.room.application.usecase.ApplyRoomUseCase;
import com.project.dorumdorum.domain.room.ui.ApplyRoomController;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplyRoomController Unit Tests")
class ApplyRoomControllerTest {

    @Mock private ApplyRoomUseCase useCase;
    @InjectMocks private ApplyRoomController controller;

    @Test
    void join_CallsUseCase() {
        JoinRoomRequest req = new JoinRoomRequest("intro", "msg");
        when(useCase.execute("u1", "r1", req)).thenReturn("req-1");
        ResponseEntity<Void> response = controller.join("u1", "r1", req);
        verify(useCase).execute("u1", "r1", req);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isEqualTo(URI.create("/api/rooms/r1/request/req-1"));
        assertThat(response.getBody()).isNull();
    }
}
