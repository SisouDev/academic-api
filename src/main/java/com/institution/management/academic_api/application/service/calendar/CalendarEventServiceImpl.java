package com.institution.management.academic_api.application.service.calendar;

import com.institution.management.academic_api.application.dto.calendar.CalendarEventDetailsDto;
import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.calendar.UpdateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.meeting.AgendaItemDto;
import com.institution.management.academic_api.application.mapper.simple.calendar.CalendarEventMapper;
import com.institution.management.academic_api.application.notifiers.calendar.CalendarEventNotifier;
import com.institution.management.academic_api.domain.factory.calendar.CalendarEventFactory;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.entities.common.Person;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.meeting.Meeting;
import com.institution.management.academic_api.domain.model.entities.user.User;
import com.institution.management.academic_api.domain.repository.calendar.CalendarEventRepository;
import com.institution.management.academic_api.domain.repository.employee.EmployeeRepository;
import com.institution.management.academic_api.domain.repository.meeting.MeetingRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.calendar.CalendarEventService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final EmployeeRepository employeeRepository;
    private final CalendarEventFactory calendarEventFactory;
    private final CalendarEventMapper calendarEventMapper;
    private final CalendarEventNotifier calendarEventNotifier;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;

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
    public List<AgendaItemDto> findVisibleEventsForUser(int year, int month) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByLogin(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuário logado não encontrado."));
        Person currentPerson = currentUser.getPerson();

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        Long departmentId = (currentPerson instanceof Employee) ? ((Employee) currentPerson).getDepartment().getId() : null;
        List<CalendarEvent> generalEvents = calendarEventRepository.findVisibleEventsForPeriod(departmentId, startOfMonth, endOfMonth);

        Stream<AgendaItemDto> generalEventDtos = generalEvents.stream()
                .map(event -> new AgendaItemDto(
                        event.getId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getType().name(),
                        false
                ));

        List<Meeting> meetings = meetingRepository.findMeetingsByPersonAndPeriod(currentPerson.getId(), startOfMonth, endOfMonth);

        Stream<AgendaItemDto> meetingDtos = meetings.stream()
                .map(meeting -> new AgendaItemDto(
                        meeting.getId(),
                        meeting.getTitle(),
                        meeting.getDescription(),
                        meeting.getStartTime(),
                        meeting.getEndTime(),
                        "MEETING",
                        true
                ));

        return Stream.concat(generalEventDtos, meetingDtos)
                .sorted(Comparator.comparing(AgendaItemDto::start))
                .collect(Collectors.toList());
    }
}
