package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.usecase.CancelRoomApplicationUseCase;
import com.project.dorumdorum.domain.room.ui.CancelRoomApplicationController;
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
@DisplayName("CancelRoomApplicationController Unit Tests")
class CancelRoomApplicationControllerTest {

    @Mock private CancelRoomApplicationUseCase useCase;
    @InjectMocks private CancelRoomApplicationController controller;

    @Test
    @DisplayName("Should cancel room application via use case")
    void cancel_CallsUseCase() {
        ResponseEntity<Void> response = controller.cancel("u1", "r1");

        verify(useCase).execute("u1", "r1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
