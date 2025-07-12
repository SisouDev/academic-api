package com.institution.management.academic_api.application.mapper.simple.calendar;

import com.institution.management.academic_api.application.dto.calendar.CalendarEventDetailsDto;
import com.institution.management.academic_api.application.dto.calendar.CalendarEventSummaryDto;
import com.institution.management.academic_api.application.dto.calendar.CreateCalendarEventRequestDto;
import com.institution.management.academic_api.application.dto.calendar.UpdateCalendarEventRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.domain.model.entities.calendar.CalendarEvent;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface CalendarEventMapper {
    @Mapping(target = "type", expression = "java(event.getType().getDisplayName())")
    @Mapping(target = "scopeDisplay", source = ".", qualifiedByName = "mapScopeDisplay")
    CalendarEventDetailsDto toDetailsDto(CalendarEvent event);

    @Mapping(target = "type", source = "type.displayName")
    CalendarEventSummaryDto toSummaryDto(CalendarEvent event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scope", ignore = true)
    @Mapping(target = "targetDepartment", ignore = true)
    @Mapping(target = "sourceEntityId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CalendarEvent toEntity(CreateCalendarEventRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scope", ignore = true)
    @Mapping(target = "targetDepartment", ignore = true)
    @Mapping(target = "sourceEntityId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(UpdateCalendarEventRequestDto dto, @MappingTarget CalendarEvent entity);

    @Named("mapScopeDisplay")
    default String mapScopeDisplay(CalendarEvent event) {
        if (event.getScope() == AnnouncementScope.DEPARTMENT && event.getTargetDepartment() != null) {
            return event.getTargetDepartment().getName();
        }
        return "Institucional";
    }
}