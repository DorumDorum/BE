package com.project.dorumdorum.domain.calendar.unit.usecase;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import com.project.dorumdorum.domain.calendar.application.mapper.CalendarEventMapper;
import com.project.dorumdorum.domain.calendar.application.usecase.LoadCalendarEventsUseCase;
import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import com.project.dorumdorum.domain.calendar.domain.repository.CalendarEventRepository;
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
@DisplayName("LoadCalendarEventsUseCase Unit Tests")
class LoadCalendarEventsUseCaseTest {

    @Mock private CalendarEventRepository calendarEventRepository;
    @Mock private CalendarEventMapper calendarEventMapper;
    @InjectMocks private LoadCalendarEventsUseCase useCase;

    @Test
    void execute_LoadsEventsAndMapsResponse() {
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 1, 31);
        List<CalendarEvent> events = List.of(
                CalendarEvent.builder().eventNo("e1").eventDate(start).title("title").build()
        );
        List<CalendarEventResponse> responses = List.of(new CalendarEventResponse(start, "title"));

        when(calendarEventRepository.findByEventDateBetween(start, end)).thenReturn(events);
        when(calendarEventMapper.toResponseList(events)).thenReturn(responses);

        List<CalendarEventResponse> result = useCase.execute(start, end);

        assertThat(result).isEqualTo(responses);
        verify(calendarEventRepository).findByEventDateBetween(start, end);
        verify(calendarEventMapper).toResponseList(events);
    }
}
