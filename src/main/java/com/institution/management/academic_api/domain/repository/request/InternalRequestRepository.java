package com.institution.management.academic_api.domain.repository.request;

import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalRequestRepository extends JpaRepository<InternalRequest, Long> {
    List<InternalRequest> findByRequesterId(Long requesterId);

    List<InternalRequest> findByHandlerId(Long handlerId);

    List<InternalRequest> findByStatus(RequestStatus status);
}
