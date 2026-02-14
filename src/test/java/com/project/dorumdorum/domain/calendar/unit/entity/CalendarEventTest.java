package com.project.dorumdorum.domain.calendar.unit.entity;

import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CalendarEvent Entity Unit Tests")
class CalendarEventTest {

    @Test
    void builder_SetsAllFields() {
        LocalDate date = LocalDate.of(2026, 2, 14);
        CalendarEvent event = CalendarEvent.builder()
                .eventNo("e1")
                .eventDate(date)
                .title("Dormitory event")
                .build();

        assertThat(event.getEventNo()).isEqualTo("e1");
        assertThat(event.getEventDate()).isEqualTo(date);
        assertThat(event.getTitle()).isEqualTo("Dormitory event");
    }
}
