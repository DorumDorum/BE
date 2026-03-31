package com.project.dorumdorum.domain.calendar.application.usecase;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import com.project.dorumdorum.domain.calendar.application.mapper.CalendarEventMapper;
import com.project.dorumdorum.domain.calendar.domain.entity.CalendarEvent;
import com.project.dorumdorum.domain.calendar.domain.service.CalendarEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadCalendarEventsUseCase {

    private final CalendarEventService calendarEventService;
    private final CalendarEventMapper calendarEventMapper;

    /**
     * 기간 내 캘린더 일정 조회
     * - 시작일~종료일 범위의 일정을 조회
     * - 응답 DTO 목록으로 변환해 반환
     */
    public List<CalendarEventResponse> execute(LocalDate startDate, LocalDate endDate) {
        List<CalendarEvent> events = calendarEventService.loadBetween(startDate, endDate);
        return calendarEventMapper.toResponseList(events);
    }
}
