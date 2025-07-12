package com.institution.management.academic_api.domain.repository.student;

import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findMostRecentByEnrollmentAndDate(@Param("enrollment") Enrollment enrollment, @Param("date") LocalDate date);

    Optional<AttendanceRecord> findByEnrollmentAndDate(Enrollment enrollment, LocalDate date);

    long countByEnrollmentAndWasPresent(Enrollment enrollment, boolean wasPresent);

    long countByEnrollment_Student(Student student);

    long countByEnrollment_StudentAndWasPresent(Student student, Boolean wasPresent);
}