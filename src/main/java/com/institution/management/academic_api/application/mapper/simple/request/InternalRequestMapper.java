package com.institution.management.academic_api.application.mapper.simple.request;

import com.institution.management.academic_api.application.dto.request.CreateInternalRequestDto;
import com.institution.management.academic_api.application.dto.request.InternalRequestDetailsDto;
import com.institution.management.academic_api.application.dto.request.InternalRequestSummaryDto;
import com.institution.management.academic_api.application.dto.request.UpdateInternalRequestDto;
import com.institution.management.academic_api.application.mapper.simple.academic.DepartmentMapper;
import com.institution.management.academic_api.application.mapper.simple.common.PersonMapper;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        PersonMapper.class,
        DepartmentMapper.class
})
public interface InternalRequestMapper {
    @Mapping(source = "requester", target = "requester")
    @Mapping(source = "handler", target = "handler")
    @Mapping(source = "targetDepartment", target = "targetDepartment")
    InternalRequestDetailsDto toDetailsDto(InternalRequest internalRequest);

    @Mapping(target = "requesterName", source = "requester", qualifiedByName = "personToFullName")
    InternalRequestSummaryDto toSummaryDto(InternalRequest internalRequest);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "handler", ignore = true)
    @Mapping(target = "targetDepartment", ignore = true)
    @Mapping(target = "resolutionNotes", ignore = true)
    InternalRequest toEntity(CreateInternalRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "urgency", ignore = true)
    @Mapping(target = "requester", ignore = true)
    @Mapping(target = "targetDepartment", ignore = true)
    @Mapping(target = "handler", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    void updateFromDto(UpdateInternalRequestDto dto, @MappingTarget InternalRequest entity);

}