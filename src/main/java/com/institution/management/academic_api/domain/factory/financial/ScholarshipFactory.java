package com.institution.management.academic_api.domain.factory.financial;

import com.institution.management.academic_api.application.dto.financial.CreateScholarshipRequestDto;
import com.institution.management.academic_api.domain.model.entities.financial.Scholarship;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.financial.ScholarshipStatus;
import org.springframework.stereotype.Component;

@Component
public class ScholarshipFactory {

    public Scholarship create(CreateScholarshipRequestDto dto, Enrollment enrollment) {
        Scholarship scholarship = new Scholarship();
        scholarship.setEnrollment(enrollment);
        scholarship.setName(dto.name());
        scholarship.setDiscountType(dto.discountType());
        scholarship.setValue(dto.value());
        scholarship.setStartDate(dto.startDate());
        scholarship.setEndDate(dto.endDate());
        scholarship.setStatus(ScholarshipStatus.ACTIVE);
        return scholarship;
    }
}