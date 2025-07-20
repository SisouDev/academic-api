package com.institution.management.academic_api.domain.service.common;

import com.institution.management.academic_api.application.dto.common.CreateSalaryStructureRequestDto;
import com.institution.management.academic_api.application.dto.common.SalaryStructureDto;
import com.institution.management.academic_api.application.dto.common.UpdateSalaryStructureRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalaryStructureService {
    SalaryStructureDto create(CreateSalaryStructureRequestDto dto);
    SalaryStructureDto findById(Long id);
    Page<SalaryStructureDto> findAll(Pageable pageable);
    SalaryStructureDto update(Long id, UpdateSalaryStructureRequestDto dto);
    void delete(Long id);
}