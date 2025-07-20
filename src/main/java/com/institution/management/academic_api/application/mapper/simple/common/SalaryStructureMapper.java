package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.CreateSalaryStructureRequestDto;
import com.institution.management.academic_api.application.dto.common.SalaryStructureDto;
import com.institution.management.academic_api.application.dto.common.UpdateSalaryStructureRequestDto;
import com.institution.management.academic_api.domain.model.entities.common.SalaryStructure;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SalaryStructureMapper {

    SalaryStructureDto toDto(SalaryStructure salaryStructure);

    @Mapping(target = "id", ignore = true)
    SalaryStructure toEntity(CreateSalaryStructureRequestDto createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobPosition", ignore = true)
    @Mapping(target = "level", ignore = true)
    void updateFromDto(UpdateSalaryStructureRequestDto updateDto, @MappingTarget SalaryStructure entity);
}