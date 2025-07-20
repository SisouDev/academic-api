package com.institution.management.academic_api.application.mapper.simple.financial;

import com.institution.management.academic_api.application.dto.financial.PurchaseOrderDto;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    @Mapping(target = "requesterName", source = "requester.firstName")
    PurchaseOrderDto toDto(PurchaseOrder purchaseOrder);
}