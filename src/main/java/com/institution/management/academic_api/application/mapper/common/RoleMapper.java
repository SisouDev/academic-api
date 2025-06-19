package com.institution.management.academic_api.application.mapper.common;

import com.institution.management.academic_api.application.dto.common.RoleDto;
import com.institution.management.academic_api.domain.model.entities.common.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDto toDto(Role role);

}