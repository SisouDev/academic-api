package com.institution.management.academic_api.domain.factory.student;
import com.institution.management.academic_api.application.dto.student.CreateEnrollmentRequestDto;
import com.institution.management.academic_api.application.mapper.student.EnrollmentMapper;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EnrollmentFactory {

    private final EnrollmentMapper enrollmentMapper;

    public Enrollment create(CreateEnrollmentRequestDto requestDto) {
        Enrollment enrollment = enrollmentMapper.toEntity(requestDto);

        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        enrollment.setTotalAbsences(0);

        return enrollment;
    }
}