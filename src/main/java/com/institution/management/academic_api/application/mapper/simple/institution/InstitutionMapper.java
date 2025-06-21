package com.institution.management.academic_api.application.mapper.simple.institution;

import com.institution.management.academic_api.application.dto.institution.CreateInstitutionRequestDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionDetailsDto;
import com.institution.management.academic_api.application.dto.institution.InstitutionSummaryDto;
import com.institution.management.academic_api.application.dto.institution.UpdateInstitutionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.simple.common.AddressMapper;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.institution.Institution;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {
        AddressMapper.class,
        DepartmentMapper.class,
        PersonMapper.class
})
public interface InstitutionMapper {

    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    InstitutionSummaryDto toSummaryDto(Institution institution);

    @Mapping(
            target = "membersCount",
            expression = "java(institution.getMembers() != null ? institution.getMembers().size() : 0)"
    )
    InstitutionDetailsDto toDetailsDto(Institution institution);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "academicTerms", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "admins", ignore = true)
    @Mapping(target = "address.street", source = "address.street")
    @Mapping(target = "address.number", source = "address.number")
    Institution toEntity(CreateInstitutionRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "departments", ignore = true)
    @Mapping(target = "academicTerms", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "admins", ignore = true)
    void updateFromDto(UpdateInstitutionRequestDto dto, @MappingTarget Institution entity);
}