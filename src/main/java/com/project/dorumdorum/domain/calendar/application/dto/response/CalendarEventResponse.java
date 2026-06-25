package com.project.dorumdorum.domain.calendar.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEventType;

import java.time.LocalDate;
import java.time.LocalTime;

public record CalendarEventResponse(
        LocalDate date,
        String title,
        String content,
        @JsonFormat(pattern = "HH:mm") LocalTime time,
        CalendarEventType type
) {
}
