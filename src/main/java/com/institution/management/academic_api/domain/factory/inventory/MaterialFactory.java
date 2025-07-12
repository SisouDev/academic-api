package com.institution.management.academic_api.domain.factory.inventory;

import com.institution.management.academic_api.application.dto.inventory.CreateMaterialRequestDto;
import com.institution.management.academic_api.application.mapper.simple.inventory.MaterialMapper;
import com.institution.management.academic_api.domain.model.entities.inventory.Material;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaterialFactory {

    private final MaterialMapper materialMapper;

    public Material create(CreateMaterialRequestDto dto) {
        return materialMapper.toEntity(dto);
    }
}