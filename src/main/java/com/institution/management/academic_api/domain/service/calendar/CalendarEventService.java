package com.institution.management.academic_api.domain.service.calendar;

import com.institution.management.academic_api.application.dto.calendar.CalendarEventDetailsDto;
import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.calendar.UpdateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.meeting.AgendaItemDto;

import java.util.List;

public interface CalendarEventService {
    CalendarEventDetailsDto create(CreateCalendarEventRequestDto dto);

    CalendarEventDetailsDto update(Long id, UpdateCalendarEventRequestDto dto);

    void delete(Long id);

    CalendarEventDetailsDto findById(Long id);

    List<AgendaItemDto> findVisibleEventsForUser(int year, int month);
}
