package com.institution.management.academic_api.domain.factory.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseRequestDto;
import com.institution.management.academic_api.domain.model.entities.employee.Employee;
import com.institution.management.academic_api.domain.model.entities.financial.PurchaseRequest;
import com.institution.management.academic_api.domain.model.enums.financial.PurchaseRequestStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PurchaseRequestFactory {

    public PurchaseRequest create(CreatePurchaseRequestDto dto, Employee requester) {
        PurchaseRequest request = new PurchaseRequest();
        request.setRequester(requester);
        request.setItemName(dto.itemName());
        request.setQuantity(dto.quantity());
        request.setJustification(dto.justification());
        request.setStatus(PurchaseRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return request;
    }
}