package com.institution.management.academic_api.domain.service.student;

import com.institution.management.academic_api.application.dto.student.*;

import java.util.List;

public interface EnrollmentService {
    EnrollmentDetailsDto enrollStudent(CreateEnrollmentRequestDto request);

    EnrollmentDetailsDto findById(Long id);

    List<EnrollmentSummaryDto> findEnrollmentsByStudent(Long studentId);

    void updateEnrollmentStatus(Long id, UpdateEnrollmentRequestDto request);

    void recordAttendance(CreateAttendanceRecordRequestDto request);
}
