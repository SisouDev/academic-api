package com.institution.management.academic_api.domain.service.financial;

import com.institution.management.academic_api.application.dto.financial.CreatePurchaseRequestDto;
import com.institution.management.academic_api.application.dto.financial.PurchaseRequestDto;
import com.institution.management.academic_api.domain.model.enums.financial.PurchaseRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseRequestService {
    PurchaseRequestDto create(CreatePurchaseRequestDto dto, String requesterEmail);

    PurchaseRequestDto findById(Long id);

    Page<PurchaseRequestDto> findAll(PurchaseRequestStatus status, Pageable pageable);

    void updateStatus(Long id, PurchaseRequestStatus newStatus);
}