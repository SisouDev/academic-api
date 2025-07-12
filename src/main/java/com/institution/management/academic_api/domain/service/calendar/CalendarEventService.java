package com.institution.management.academic_api.domain.service.calendar;

import com.institution.management.academic_api.application.dto.calendar.CalendarEventDetailsDto;
import com.institution.management.academic_api.application.dto.calendar.CalendarEventSummaryDto;
import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.calendar.UpdateCalendarEventRequestDto;

import java.util.List;

public interface CalendarEventService {
    CalendarEventDetailsDto create(CreateCalendarEventRequestDto dto);

    CalendarEventDetailsDto update(Long id, UpdateCalendarEventRequestDto dto);

    void delete(Long id);

    CalendarEventDetailsDto findById(Long id);

    List<CalendarEventSummaryDto> findVisibleEventsForMonth(int year, int month);
}
