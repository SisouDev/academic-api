package com.institution.management.academic_api.domain.service.humanResources;

import com.institution.management.academic_api.application.dto.humanResources.CreateLeaveRequestDto;
import com.institution.management.academic_api.application.dto.humanResources.LeaveRequestDetailsDto;
import com.institution.management.academic_api.application.dto.humanResources.ReviewLeaveRequestDto;
import com.institution.management.academic_api.domain.model.enums.humanResources.LeaveRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequestDetailsDto create(CreateLeaveRequestDto dto, String requesterEmail);

    void review(Long leaveRequestId, ReviewLeaveRequestDto dto, String reviewerEmail);

    LeaveRequestDetailsDto findById(Long id);

    List<LeaveRequestDetailsDto> findByRequester(Long requesterId);

    Page<LeaveRequestDetailsDto> findAll(LeaveRequestStatus status, Pageable pageable);

}
