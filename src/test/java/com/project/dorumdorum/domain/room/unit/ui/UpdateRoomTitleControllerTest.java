package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.dto.request.UpdateRoomTitleRequest;
import com.project.dorumdorum.domain.room.application.usecase.UpdateRoomTitleUseCase;
import com.project.dorumdorum.domain.room.ui.UpdateRoomTitleController;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateRoomTitleController Unit Tests")
class UpdateRoomTitleControllerTest {

    @Mock private UpdateRoomTitleUseCase useCase;
    @InjectMocks private UpdateRoomTitleController controller;

    @Test
    void update_CallsUseCase() {
        UpdateRoomTitleRequest req = new UpdateRoomTitleRequest("new");
        ResponseEntity<Void> response = controller.update("u1", "r1", req);
        verify(useCase).execute("u1", "r1", req);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
