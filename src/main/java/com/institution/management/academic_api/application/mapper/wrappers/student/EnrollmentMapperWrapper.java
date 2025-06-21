package com.institution.management.academic_api.application.mapper.wrappers.student;
import com.institution.management.academic_api.application.dto.student.EnrollmentDetailsDto;
import com.institution.management.academic_api.application.dto.student.EnrollmentSummaryDto;
import com.institution.management.academic_api.application.mapper.simple.student.EnrollmentMapper;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentMapperWrapper {

    @Lazy
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentSummaryDto toSummaryDto(Enrollment enrollment) {
        return enrollmentMapper.toSummaryDto(enrollment);
    }

    public EnrollmentDetailsDto toDetailsDto(Enrollment enrollment) {
        return enrollmentMapper.toDetailsDto(enrollment);
    }

}