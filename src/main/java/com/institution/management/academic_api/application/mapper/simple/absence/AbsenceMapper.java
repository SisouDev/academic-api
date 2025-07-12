package com.institution.management.academic_api.application.mapper.simple.absence;

import com.institution.management.academic_api.application.dto.absence.AbsenceDetailsDto;
import com.institution.management.academic_api.application.dto.absence.AbsenceSummaryDto;
import com.institution.management.academic_api.application.dto.absence.CreateAbsenceRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.absence.Absence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface AbsenceMapper {
    @Mapping(target = "person", source = "person")
    @Mapping(target = "reviewedBy", source = "reviewedBy")
    @Mapping(target = "type", expression = "java(absence.getType().getDisplayName())")
    @Mapping(target = "status", expression = "java(absence.getStatus().getDisplayName())")
    AbsenceDetailsDto toDetailsDto(Absence absence);

    @Mapping(target = "personName", source = "person", qualifiedByName = "personToFullName")
    @Mapping(target = "type", expression = "java(absence.getType().getDisplayName())")
    @Mapping(target = "status", expression = "java(absence.getStatus().getDisplayName())")
    AbsenceSummaryDto toSummaryDto(Absence absence);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attachmentUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    Absence toEntity(CreateAbsenceRequestDto dto);
}