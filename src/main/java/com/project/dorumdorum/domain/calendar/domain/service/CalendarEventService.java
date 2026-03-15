package com.project.dorumdorum.domain.calendar.domain.service;

import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import com.project.dorumdorum.domain.calendar.domain.repository.CalendarEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;

    @Transactional(readOnly = true)
    public List<CalendarEvent> loadBetween(LocalDate startDate, LocalDate endDate) {
        return calendarEventRepository.findByEventDateBetween(startDate, endDate);
    }
}
