package com.institution.management.academic_api.domain.factory.calendar;

import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.mapper.simple.calendar.CalendarEventMapper;
import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import com.institution.management.academic_api.domain.repository.academic.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CalendarEventFactory {

    private final CalendarEventMapper calendarEventMapper;
    private final DepartmentRepository departmentRepository;

    public CalendarEvent create(CreateCalendarEventRequestDto dto) {
        CalendarEvent event = calendarEventMapper.toEntity(dto);

        event.setCreatedAt(LocalDateTime.now());
        AnnouncementScope scope = AnnouncementScope.valueOf(dto.scope());
        event.setScope(scope);

        if (scope == AnnouncementScope.DEPARTMENT) {
            if (dto.targetDepartmentId() == null) {
                throw new IllegalArgumentException("targetDepartmentId is required for department scoped events.");
            }
            Department targetDepartment = departmentRepository.findById(dto.targetDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.targetDepartmentId()));
            event.setTargetDepartment(targetDepartment);
        } else {
            event.setTargetDepartment(null);
        }

        return event;
    }
}