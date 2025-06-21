package com.institution.management.academic_api.domain.service.student;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.student.CreateStudentRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentResponseDto;
import com.institution.management.academic_api.application.dto.student.UpdateStudentRequestDto;

import java.util.List;

public interface StudentService {
    StudentResponseDto create(CreateStudentRequestDto request);

    StudentResponseDto findById(Long id);

    List<PersonSummaryDto> findAllByInstitution(Long institutionId);

    StudentResponseDto update(Long id, UpdateStudentRequestDto request);

    void updateStatus(Long id, String status);
}
