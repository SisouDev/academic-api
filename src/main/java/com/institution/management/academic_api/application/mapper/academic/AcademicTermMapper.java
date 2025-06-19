package com.institution.management.academic_api.application.mapper.academic;

import com.institution.management.academic_api.application.dto.academic.AcademicTermDetailsDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermRequestDto;
import com.institution.management.academic_api.application.dto.academic.AcademicTermSummaryDto;
import com.institution.management.academic_api.application.dto.academic.UpdateAcademicTermRequestDto;
import com.institution.management.academic_api.application.mapper.course.CourseSectionMapper;
import com.institution.management.academic_api.application.mapper.institution.InstitutionMapper;
import com.institution.management.academic_api.domain.model.entities.academic.AcademicTerm;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {InstitutionMapper.class, CourseSectionMapper.class})
public interface AcademicTermMapper {

    AcademicTermMapper INSTANCE = Mappers.getMapper(AcademicTermMapper.class);

    AcademicTermDetailsDto toDetailsDto(AcademicTerm academicTerm);

    AcademicTermSummaryDto toSummaryDto(AcademicTerm academicTerm);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSections", ignore = true)
    @Mapping(target = "institution", ignore = true)
    AcademicTerm toEntity(AcademicTermRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "courseSections", ignore = true)
    void updateFromDto(UpdateAcademicTermRequestDto dto, @MappingTarget AcademicTerm entity);
}