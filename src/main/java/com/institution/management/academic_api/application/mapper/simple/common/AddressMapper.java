package com.institution.management.academic_api.application.mapper.simple.common;

import com.institution.management.academic_api.application.dto.common.AddressDto;
import com.institution.management.academic_api.domain.model.entities.common.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDto toDto(Address address);

    Address toEntity(AddressDto addressDto);
}