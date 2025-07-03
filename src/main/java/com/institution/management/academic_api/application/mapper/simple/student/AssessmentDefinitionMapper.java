package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDefinitionDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AssessmentDefinitionMapper {
    AssessmentDefinitionDto toDto(AssessmentDefinition definition);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    AssessmentDefinition toEntity(CreateAssessmentDefinitionRequestDto request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseSection", ignore = true)
    void updateFromDto(UpdateAssessmentDefinitionRequestDto dto, @MappingTarget AssessmentDefinition entity);
}