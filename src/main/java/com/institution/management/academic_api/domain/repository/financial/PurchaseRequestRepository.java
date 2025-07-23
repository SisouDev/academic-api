package com.institution.management.academic_api.domain.repository.financial;

import com.institution.management.academic_api.domain.model.entities.financial.PurchaseRequest;
import com.institution.management.academic_api.domain.model.enums.financial.PurchaseRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    Page<PurchaseRequest> findAllByStatus(PurchaseRequestStatus status, Pageable pageable);

}