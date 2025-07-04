package com.institution.management.academic_api.domain.service.student;

import com.institution.management.academic_api.application.dto.student.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    StudentResponseDto create(CreateStudentRequestDto request);

    StudentResponseDto findById(Long id);

    StudentResponseDto update(Long id, UpdateStudentRequestDto request);

    StudentResponseDto updateStatus(Long id, String status);

    Page<StudentSummaryDto> findPaginated(String searchTerm, Long institutionId, Pageable pageable);

    List<EnrollmentSummaryDto> findEnrollmentsForCurrentStudent();

}
