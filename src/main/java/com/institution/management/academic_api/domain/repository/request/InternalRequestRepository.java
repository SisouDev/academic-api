package com.institution.management.academic_api.domain.repository.request;

import com.institution.management.academic_api.domain.model.entities.academic.Department;
import com.institution.management.academic_api.domain.model.entities.request.InternalRequest;
import com.institution.management.academic_api.domain.model.enums.request.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalRequestRepository extends JpaRepository<InternalRequest, Long> {
    List<InternalRequest> findByRequesterId(Long requesterId);

    List<InternalRequest> findByHandlerId(Long handlerId);

    List<InternalRequest> findByStatus(RequestStatus status);

    long countByTargetDepartmentAndStatus(Department department, RequestStatus status);

    List<InternalRequest> findTop5ByTargetDepartmentAndStatusOrderByCreatedAtDesc(Department department, RequestStatus status);

    Page<InternalRequest> findAllByStatus(RequestStatus status, Pageable pageable);
}
