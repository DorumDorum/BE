package com.project.dorumdorum.domain.calendar.domain.repository;

import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, String> {
    List<CalendarEvent> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
    List<CalendarEvent> findByEventDate(LocalDate eventDate);
}
