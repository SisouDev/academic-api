package com.institution.management.academic_api.domain.service.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDefinitionDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentDefinitionRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentDefinitionRequestDto;

import java.util.List;

public interface AssessmentDefinitionService {
    AssessmentDefinitionDto create(CreateAssessmentDefinitionRequestDto request);
    List<AssessmentDefinitionDto> findByCourseSection(Long courseSectionId);
    AssessmentDefinitionDto findById(Long id);
    AssessmentDefinitionDto update(Long id, UpdateAssessmentDefinitionRequestDto request);
    void delete(Long id);
}