package com.project.dorumdorum.domain.calendar.ui;

import com.project.dorumdorum.domain.calendar.application.dto.response.CalendarEventResponse;
import com.project.dorumdorum.domain.calendar.application.usecase.LoadCalendarEventsUseCase;
import com.project.dorumdorum.domain.calendar.ui.spec.LoadCalendarEventsApiSpec;
import com.project.dorumdorum.global.annotation.AccessToken;
import com.project.dorumdorum.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoadCalendarEventsController implements LoadCalendarEventsApiSpec {

    private final LoadCalendarEventsUseCase loadCalendarEventsUseCase;

    @Override
    @GetMapping("/api/calendar/events")
    public BaseResponse<List<CalendarEventResponse>> loadCalendarEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AccessToken Long userNo
    ) {
        List<CalendarEventResponse> events = loadCalendarEventsUseCase.execute(startDate, endDate);
        return BaseResponse.onSuccess(events);
    }
}
