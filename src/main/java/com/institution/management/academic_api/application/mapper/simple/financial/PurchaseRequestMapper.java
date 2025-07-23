package com.institution.management.academic_api.application.mapper.simple.financial;

import com.institution.management.academic_api.application.dto.financial.PurchaseRequestDto;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseRequestMapper {

    @Mapping(target = "requesterName", source = "requester.firstName")
    PurchaseRequestDto toDto(PurchaseRequest purchaseRequest);
}