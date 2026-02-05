package com.project.dorumdorum.domain.calendar.application.dto.response;

import java.time.LocalDate;

public record CalendarEventResponse(
        LocalDate date,
        String title
) {
}
