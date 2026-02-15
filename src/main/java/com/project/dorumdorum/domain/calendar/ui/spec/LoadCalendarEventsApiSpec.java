package com.project.dorumdorum.domain.calendar.ui.spec;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Calendar", description = "기숙사 캘린더 API")
public interface LoadCalendarEventsApiSpec {

    @Operation(summary = "캘린더 일정 조회", description = "지정된 기간의 기숙사 일정을 조회합니다.")
    @GetMapping("/api/calendar/events")
    ResponseEntity<List<CalendarEventResponse>> loadCalendarEvents(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)", example = "2025-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)", example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    );
}
