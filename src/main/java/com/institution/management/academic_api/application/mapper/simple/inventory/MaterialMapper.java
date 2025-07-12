package com.institution.management.academic_api.application.mapper.simple.inventory;

import com.institution.management.academic_api.application.dto.inventory.CreateMaterialRequestDto;
import com.institution.management.academic_api.application.dto.inventory.MaterialDetailsDto;
import com.institution.management.academic_api.application.dto.inventory.MaterialSummaryDto;
import com.institution.management.academic_api.application.dto.inventory.UpdateMaterialRequestDto;
import com.institution.management.academic_api.domain.model.entities.inventory.Material;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    MaterialDetailsDto toDetailsDto(Material material);

    MaterialSummaryDto toSummaryDto(Material material);


    @Mapping(target = "id", ignore = true)
    Material toEntity(CreateMaterialRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateFromDto(UpdateMaterialRequestDto dto, @MappingTarget Material entity);
}