package com.institution.management.academic_api.domain.service.humanResources;

import com.institution.management.academic_api.application.dto.humanResources.CreateLeaveRequestDto;
import com.institution.management.academic_api.application.dto.humanResources.LeaveRequestDetailsDto;
import com.institution.management.academic_api.application.dto.humanResources.ReviewLeaveRequestDto;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequestDetailsDto create(CreateLeaveRequestDto dto, String requesterEmail);

    void review(Long leaveRequestId, ReviewLeaveRequestDto dto, String reviewerEmail);

    LeaveRequestDetailsDto findById(Long id);

    List<LeaveRequestDetailsDto> findByRequester(Long requesterId);
}
