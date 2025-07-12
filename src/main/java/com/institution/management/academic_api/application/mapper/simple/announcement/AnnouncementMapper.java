package com.institution.management.academic_api.application.mapper.simple.announcement;

import com.institution.management.academic_api.application.dto.announcement.AnnouncementDetailsDto;
import com.institution.management.academic_api.application.dto.announcement.AnnouncementSummaryDto;
import com.institution.management.academic_api.application.dto.announcement.CreateAnnouncementRequestDto;
import com.institution.management.academic_api.application.dto.announcement.UpdateAnnouncementRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.announcement.Announcement;
import com.institution.management.academic_api.domain.model.enums.announcement.AnnouncementScope;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        PersonMapper.class,
        DepartmentMapper.class
})
public interface AnnouncementMapper {
    AnnouncementDetailsDto toDetailsDto(Announcement announcement);

    @Mapping(target = "createdByFullName", source = "createdBy", qualifiedByName = "personToFullName")
    @Mapping(target = "scopeDisplay", expression = "java(mapScopeDisplay(announcement))")
    AnnouncementSummaryDto toSummaryDto(Announcement announcement);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "targetDepartment", ignore = true)
    Announcement toEntity(CreateAnnouncementRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scope", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "targetDepartment", ignore = true)
    void updateFromDto(UpdateAnnouncementRequestDto dto, @MappingTarget Announcement entity);


    @Named("mapScopeDisplay")
    default String mapScopeDisplay(Announcement announcement) {
        if (announcement.getScope() == AnnouncementScope.DEPARTMENT && announcement.getTargetDepartment() != null) {
            return announcement.getTargetDepartment().getName();
        }
        return "Institucional";
    }
}