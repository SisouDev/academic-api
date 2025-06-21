package com.institution.management.academic_api.application.mapper.simple.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentRequestDto;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssessmentMapper {

    AssessmentMapper INSTANCE = Mappers.getMapper(AssessmentMapper.class);

    AssessmentDto toDto(Assessment assessment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    Assessment toEntity(CreateAssessmentRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollment", ignore = true)
    void updateFromDto(UpdateAssessmentRequestDto dto, @MappingTarget Assessment entity);
}