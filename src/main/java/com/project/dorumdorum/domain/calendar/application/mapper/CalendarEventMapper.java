package com.project.dorumdorum.domain.calendar.application.mapper;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CalendarEventMapper {
    @Mapping(target = "date", source = "eventDate")
    @Mapping(target = "time", source = "eventTime")
    @Mapping(target = "type", source = "eventType")
    CalendarEventResponse toResponse(CalendarEvent event);
    List<CalendarEventResponse> toResponseList(List<CalendarEvent> events);
}
