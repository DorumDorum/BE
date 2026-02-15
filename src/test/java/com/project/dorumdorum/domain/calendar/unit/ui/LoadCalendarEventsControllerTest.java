package com.project.dorumdorum.domain.calendar.unit.ui;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import com.project.dorumdorum.domain.calendar.application.usecase.LoadCalendarEventsUseCase;
import com.project.dorumdorum.domain.calendar.ui.LoadCalendarEventsController;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoadCalendarEventsController Unit Tests")
class LoadCalendarEventsControllerTest {

    @Mock private LoadCalendarEventsUseCase loadCalendarEventsUseCase;
    @InjectMocks private LoadCalendarEventsController controller;

    @Test
    void loadCalendarEvents_ReturnsUseCaseResult() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);
        List<CalendarEventResponse> payload = List.of(new CalendarEventResponse(start, "event"));
        when(loadCalendarEventsUseCase.execute(start, end)).thenReturn(payload);

        ResponseEntity<List<CalendarEventResponse>> response = controller.loadCalendarEvents(start, end);

        verify(loadCalendarEventsUseCase).execute(start, end);
        assertThat(response.getBody()).isEqualTo(payload);
    }
}
