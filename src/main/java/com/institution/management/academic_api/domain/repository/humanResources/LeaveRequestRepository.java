package com.institution.management.academic_api.domain.repository.humanResources;

import com.institution.management.academic_api.domain.model.entities.humanResources.LeaveRequest;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository <LeaveRequest, Long>{
    List<LeaveRequest> findByRequesterId(Long requesterId);

    List<LeaveRequest> findByStatus(LeaveRequestStatus status);

    long countByStatus(LeaveRequestStatus status);

    List<LeaveRequest> findTop5ByStatusOrderByCreatedAtDesc(LeaveRequestStatus status);

}
