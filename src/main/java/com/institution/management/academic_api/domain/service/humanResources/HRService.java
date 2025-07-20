package com.institution.management.academic_api.domain.service.humanResources;

import com.institution.management.academic_api.application.dto.employee.CreateJobHistoryRequestDto;
import com.institution.management.academic_api.application.dto.employee.JobHistoryDto;

import java.util.List;

public interface HRService {
    void recordJobHistoryEvent(CreateJobHistoryRequestDto dto);
    List<JobHistoryDto> findJobHistoryByPerson(Long personId);

    JobHistoryDto findJobHistoryById(Long id);

}
