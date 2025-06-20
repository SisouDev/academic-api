package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByEnrollmentOrderByDate(Enrollment enrollment);

    long countByEnrollmentAndWasPresent(Enrollment enrollment, boolean wasPresent);
}
