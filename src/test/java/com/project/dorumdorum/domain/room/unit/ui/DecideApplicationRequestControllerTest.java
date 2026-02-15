package com.project.dorumdorum.domain.room.unit.ui;

import com.project.dorumdorum.domain.room.application.usecase.DecideApplicationRequestUseCase;
import com.project.dorumdorum.domain.room.ui.DecideApplicationRequestController;
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
@DisplayName("DecideApplicationRequestController Unit Tests")
class DecideApplicationRequestControllerTest {

    @Mock private DecideApplicationRequestUseCase useCase;
    @InjectMocks private DecideApplicationRequestController controller;

    @Test
    @DisplayName("Should approve application request via use case")
    void approve_CallsUseCase() {
        ResponseEntity<Void> response = controller.approve("host", "r1", "rq1");

        verify(useCase).approve("host", "r1", "rq1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("Should reject application request via use case")
    void reject_CallsUseCase() {
        ResponseEntity<Void> response = controller.reject("host", "rq1");

        verify(useCase).reject("host", "rq1");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNull();
    }
}
