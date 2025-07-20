package com.institution.management.academic_api.domain.factory.common;

import com.institution.management.academic_api.application.dto.common.CreateSalaryStructureRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.SalaryStructureMapper;
import com.institution.management.academic_api.domain.model.entities.common.SalaryStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SalaryStructureFactory {

    private final SalaryStructureMapper mapper;

    public SalaryStructure create(CreateSalaryStructureRequestDto dto) {
        return mapper.toEntity(dto);
    }
}