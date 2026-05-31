package com.project.dorumdorum.domain.calendar.unit.response;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CalendarEventResponse Unit Tests")
class CalendarEventResponseTest {

    @Test
    void record_AccessorsReturnValues() {
        LocalDate date = LocalDate.of(2026, 2, 14);
        CalendarEventResponse response = new CalendarEventResponse(date, "event", null, null, null);

        assertThat(response.date()).isEqualTo(date);
        assertThat(response.title()).isEqualTo("event");
    }
}
