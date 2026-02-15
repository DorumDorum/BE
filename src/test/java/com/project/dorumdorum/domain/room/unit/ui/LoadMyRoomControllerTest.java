package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.response.FindRoomsResponse;
import com.project.dorumdorum.domain.room.application.usecase.LoadMyRoomsUseCase;
import com.project.dorumdorum.domain.room.ui.LoadMyRoomController;
import org.springframework.http.ResponseEntity;
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
@DisplayName("LoadMyRoomController Unit Tests")
class LoadMyRoomControllerTest {

    @Mock private LoadMyRoomsUseCase useCase;
    @InjectMocks private LoadMyRoomController controller;

    @Test
    void load_ReturnsUseCaseResponse() {
        FindRoomsResponse payload = org.mockito.Mockito.mock(FindRoomsResponse.class);
        when(useCase.execute("u1")).thenReturn(payload);

        ResponseEntity<FindRoomsResponse> response = controller.load("u1");

        verify(useCase).execute("u1");
        assertThat(response.getBody()).isEqualTo(payload);
    }
}
