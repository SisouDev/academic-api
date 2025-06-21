package com.institution.management.academic_api.application.service.student;

import com.institution.management.academic_api.application.dto.student.*;
import com.institution.management.academic_api.application.mapper.simple.student.EnrollmentMapper;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import com.institution.management.academic_api.domain.model.entities.student.AttendanceRecord;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.entities.student.Student;
import com.institution.management.academic_api.domain.model.enums.academic.AcademicTermStatus;
import com.institution.management.academic_api.domain.model.enums.common.PersonStatus;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.repository.course.CourseSectionRepository;
import com.institution.management.academic_api.domain.repository.student.AttendanceRecordRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.repository.student.StudentRepository;
import com.institution.management.academic_api.domain.service.student.EnrollmentService;
import com.institution.management.academic_api.exception.type.common.InvalidOperationException;
import com.institution.management.academic_api.exception.type.course.CourseSectionNotFoundException;
import com.institution.management.academic_api.exception.type.student.EnrollmentNotFoundException;
import com.institution.management.academic_api.exception.type.student.StudentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    private final CourseSectionRepository courseSectionRepository;
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public EnrollmentDetailsDto enrollStudent(CreateEnrollmentRequestDto request) {
        Student student = findStudentByIdOrThrow(request.studentId());
        CourseSection courseSection = findCourseSectionByIdOrThrow(request.courseSectionId());
        if (courseSection.getAcademicTerm().getStatus() != AcademicTermStatus.ENROLLMENT_OPEN) {
            throw new InvalidOperationException("The registration period for this class is not open.");
        }
        if (student.getStatus() != PersonStatus.ACTIVE) {
            throw new InvalidOperationException("The student " + student.getFirstName() + " is not active and cannot be enrolled.");
        }
        if (enrollmentRepository.existsByStudentAndCourseSection(student, courseSection)) {
            throw new InvalidOperationException("This student is already enrolled in this class.");
        }
        Enrollment newEnrollment = enrollmentMapper.toEntity(request);
        newEnrollment.setStudent(student);
        newEnrollment.setCourseSection(courseSection);
        newEnrollment.setEnrollmentDate(LocalDate.now());
        newEnrollment.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment savedEnrollment = enrollmentRepository.save(newEnrollment);
        return enrollmentMapper.toDetailsDto(savedEnrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDetailsDto findById(Long id) {
        Enrollment enrollment = findEnrollmentByIdOrThrow(id);
        return enrollmentMapper.toDetailsDto(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentSummaryDto> findEnrollmentsByStudent(Long studentId) {
        Student student = findStudentByIdOrThrow(studentId);
        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent(student);
        return enrollments.stream()
                .map(enrollmentMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateEnrollmentStatus(Long id, UpdateEnrollmentRequestDto request) {
        Enrollment enrollmentToUpdate = findEnrollmentByIdOrThrow(id);
        if (request.status() != null && !request.status().isBlank()) {
            EnrollmentStatus newStatus = EnrollmentStatus.valueOf(request.status().toUpperCase());
            enrollmentToUpdate.setStatus(newStatus);
        }
        if (request.totalAbsences() != null) {
            enrollmentToUpdate.setTotalAbsences(request.totalAbsences());
        }
    }

    @Override
    @Transactional
    public void recordAttendance(CreateAttendanceRecordRequestDto request) {
        Enrollment enrollment = findEnrollmentByIdOrThrow(request.enrollmentId());
        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
            throw new InvalidOperationException("It is not possible to register attendance for a registration that is not active.");
        }
        AttendanceRecord newRecord = new AttendanceRecord();
        newRecord.setEnrollment(enrollment);
        newRecord.setDate(request.date());
        newRecord.setWasPresent(request.wasPresent());

        if (Boolean.FALSE.equals(request.wasPresent())) {
            enrollment.setTotalAbsences(enrollment.getTotalAbsences() + 1);
        }

        attendanceRecordRepository.save(newRecord);
    }

    private Student findStudentByIdOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
    }

    private CourseSection findCourseSectionByIdOrThrow(Long id) {
        return courseSectionRepository.findById(id)
                .orElseThrow(() -> new CourseSectionNotFoundException("Course Section not found with id: " + id));
    }

    private Enrollment findEnrollmentByIdOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));
    }
}
