package com.institution.management.academic_api.application.mapper.simple.institution;

import com.institution.management.academic_api.application.dto.institution.CreateInstitutionAdminRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.mapper.simple.common.DocumentMapper;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface InstitutionAdminMapper {

    InstitutionAdminResponseDto toDto(InstitutionAdmin admin);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "institution", ignore = true)
    @Mapping(target = "document.type", source = "document.type")
    @Mapping(target = "document.number", source = "document.number")
    InstitutionAdmin toEntity(CreateInstitutionAdminRequestDto requestDto);

}