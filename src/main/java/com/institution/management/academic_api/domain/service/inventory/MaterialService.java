package com.institution.management.academic_api.domain.service.inventory;

import com.institution.management.academic_api.application.dto.inventory.CreateMaterialRequestDto;
import com.institution.management.academic_api.application.dto.inventory.MaterialDetailsDto;
import com.institution.management.academic_api.application.dto.inventory.UpdateMaterialRequestDto;

import java.util.List;

public interface MaterialService {
    MaterialDetailsDto create(CreateMaterialRequestDto dto);

    MaterialDetailsDto update(Long id, UpdateMaterialRequestDto dto);

    void delete(Long id);

    MaterialDetailsDto findById(Long id);

    List<MaterialDetailsDto> findAll();
}
