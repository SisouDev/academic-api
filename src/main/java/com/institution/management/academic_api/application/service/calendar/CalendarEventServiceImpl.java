package com.institution.management.academic_api.application.service.calendar;

import com.institution.management.academic_api.application.dto.calendar.CalendarEventDetailsDto;
import com.institution.management.academic_api.application.dto.calendar.CalendarEventSummaryDto;
import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.calendar.UpdateCalendarEventRequestDto;
import com.institution.management.academic_api.application.mapper.simple.calendar.CalendarEventMapper;
import com.institution.management.academic_api.application.notifiers.calendar.CalendarEventNotifier;
import com.institution.management.academic_api.domain.factory.calendar.CalendarEventFactory;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.repository.calendar.CalendarEventRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.service.calendar.CalendarEventService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final EmployeeRepository employeeRepository;
    private final CalendarEventFactory calendarEventFactory;
    private final CalendarEventMapper calendarEventMapper;
    private final CalendarEventNotifier calendarEventNotifier;

    @Override
    @Transactional
    @LogActivity("Criou um novo evento no calendário.")
    public CalendarEventDetailsDto create(CreateCalendarEventRequestDto dto) {
        CalendarEvent newEvent = calendarEventFactory.create(dto);
        CalendarEvent savedEvent = calendarEventRepository.save(newEvent);
        calendarEventNotifier.notifyNewEvent(savedEvent);
        return calendarEventMapper.toDetailsDto(savedEvent);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou um evento do calendário.")
    public CalendarEventDetailsDto update(Long id, UpdateCalendarEventRequestDto dto) {
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        calendarEventMapper.updateFromDto(dto, event);
        calendarEventNotifier.notifyEventUpdate(event);
        return calendarEventMapper.toDetailsDto(event);
    }

    @Override
    @Transactional
    @LogActivity("Deletou um evento do calendário.")
    public void delete(Long id) {
        CalendarEvent event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
        calendarEventNotifier.notifyEventCancellation(event);
        calendarEventRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CalendarEventDetailsDto findById(Long id) {
        return calendarEventRepository.findById(id)
                .map(calendarEventMapper::toDetailsDto)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarEventSummaryDto> findVisibleEventsForMonth(int year, int month) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee currentUser = employeeRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Actual user not found.."));

        Long departmentId = (currentUser.getDepartment() != null) ? currentUser.getDepartment().getId() : null;

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();

        return calendarEventRepository.findVisibleEventsForPeriod(departmentId, startOfMonth, endOfMonth).stream()
                .map(calendarEventMapper::toSummaryDto)
                .collect(Collectors.toList());
    }
}
