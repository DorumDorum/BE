package com.project.dorumdorum.domain.calendar.unit.mapper;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import com.project.dorumdorum.domain.calendar.application.mapper.CalendarEventMapper;
import com.project.dorumdorum.domain.calendar.application.mapper.CalendarEventMapperImpl;
import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CalendarEventMapper Unit Tests")
class CalendarEventMapperTest {

    private final CalendarEventMapper mapper = new CalendarEventMapperImpl();

    @Test
    void toResponse_MapsSingleEvent() {
        CalendarEvent event = CalendarEvent.builder()
                .eventNo("e1")
                .eventDate(LocalDate.of(2026, 2, 14))
                .title("event")
                .build();

        CalendarEventResponse response = mapper.toResponse(event);

        assertThat(response.date()).isEqualTo(event.getEventDate());
        assertThat(response.title()).isEqualTo(event.getTitle());
    }

    @Test
    void toResponseList_MapsEventList() {
        List<CalendarEvent> events = List.of(
                CalendarEvent.builder().eventNo("e1").eventDate(LocalDate.of(2026, 2, 14)).title("a").build(),
                CalendarEvent.builder().eventNo("e2").eventDate(LocalDate.of(2026, 2, 15)).title("b").build()
        );

        List<CalendarEventResponse> responses = mapper.toResponseList(events);

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).title()).isEqualTo("a");
        assertThat(responses.get(1).title()).isEqualTo("b");
    }

    @Test
    void nullInputs_ReturnNull() {
        assertThat(mapper.toResponse(null)).isNull();
        assertThat(mapper.toResponseList(null)).isNull();
    }
}
