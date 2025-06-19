package com.institution.management.academic_api.application.mapper.institution;

import com.institution.management.academic_api.application.dto.institution.InstitutionAdminResponseDto;
import com.institution.management.academic_api.application.mapper.common.DocumentMapper;
import com.institution.management.academic_api.domain.model.entities.institution.InstitutionAdmin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DocumentMapper.class})
public interface InstitutionAdminMapper {

    InstitutionAdminResponseDto toDto(InstitutionAdmin admin);

}