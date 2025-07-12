package com.institution.management.academic_api.application.mapper.simple.it;

import com.institution.management.academic_api.application.dto.it.AssetDetailsDto;
import com.institution.management.academic_api.application.dto.it.AssetSummaryDto;
import com.institution.management.academic_api.application.dto.it.CreateAssetRequestDto;
import com.institution.management.academic_api.application.dto.it.UpdateAssetRequestDto;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.it.Asset;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface AssetMapper {
    @Mapping(target = "status", expression = "java(asset.getStatus().getDisplayName())")
    @Mapping(target = "assignedTo", source = "assignedTo")
    AssetDetailsDto toDetailsDto(Asset asset);

    @Mapping(target = "status", expression = "java(asset.getStatus().getDisplayName())")
    @Mapping(target = "assignedToName", source = "assignedTo", qualifiedByName = "personToFullName")
    AssetSummaryDto toSummaryDto(Asset asset);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Asset toEntity(CreateAssetRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "assetTag", ignore = true)
    @Mapping(target = "serialNumber", ignore = true)
    @Mapping(target = "purchaseDate", ignore = true)
    @Mapping(target = "purchaseCost", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateFromDto(UpdateAssetRequestDto dto, @MappingTarget Asset entity);
}