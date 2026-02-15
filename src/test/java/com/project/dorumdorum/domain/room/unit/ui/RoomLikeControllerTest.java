package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.usecase.RoomLikeUseCase;
import com.project.dorumdorum.domain.room.ui.RoomLikeController;
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
@DisplayName("RoomLikeController Unit Tests")
class RoomLikeControllerTest {

    @Mock private RoomLikeUseCase useCase;
    @InjectMocks private RoomLikeController controller;

    @Test
    void like_CallsUseCase() {
        ResponseEntity<Void> response = controller.like("u1", "r1");
        verify(useCase).like("u1", "r1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }

    @Test
    void unlike_CallsUseCase() {
        ResponseEntity<Void> response = controller.unlike("u1", "r1");
        verify(useCase).unlike("u1", "r1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
