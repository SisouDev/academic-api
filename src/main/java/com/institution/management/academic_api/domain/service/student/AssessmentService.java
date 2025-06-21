package com.institution.management.academic_api.domain.service.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentRequestDto;

import java.util.List;

public interface AssessmentService {
    AssessmentDto addAssessmentToEnrollment(CreateAssessmentRequestDto request);

    AssessmentDto updateAssessment(Long assessmentId, UpdateAssessmentRequestDto request);

    void deleteAssessment(Long assessmentId);

    List<AssessmentDto> findAssessmentsByEnrollment(Long enrollmentId);
}
