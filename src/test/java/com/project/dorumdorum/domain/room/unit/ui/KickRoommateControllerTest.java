package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.usecase.KickRoommateUseCase;
import com.project.dorumdorum.domain.room.ui.KickRoommateController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("KickRoommateController Unit Tests")
class KickRoommateControllerTest {

    @Mock private KickRoommateUseCase useCase;
    @InjectMocks private KickRoommateController controller;

    @Test
    @DisplayName("UseCase를 호출하고 200 응답을 반환한다")
    void kick_CallsUseCaseAndReturnsOk() {
        ResponseEntity<Void> response = controller.kick("host", "r1", "member1");

        verify(useCase).execute("host", "r1", "member1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
