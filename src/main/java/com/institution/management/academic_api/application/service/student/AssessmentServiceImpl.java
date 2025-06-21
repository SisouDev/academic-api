package com.institution.management.academic_api.application.service.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.AssessmentMapper;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.model.enums.student.EnrollmentStatus;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.service.student.AssessmentService;
import com.institution.management.academic_api.exception.type.common.InvalidOperationException;
import com.institution.management.academic_api.exception.type.student.AssessmentNotFoundException;
import com.institution.management.academic_api.exception.type.student.EnrollmentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentMapper assessmentMapper;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public AssessmentDto addAssessmentToEnrollment(CreateAssessmentRequestDto request) {
        Enrollment enrollment = findEnrollmentByIdOrThrow(request.enrollmentId());
        if (enrollment.getStatus() != EnrollmentStatus.ACTIVE){
            throw new InvalidOperationException("The student " + enrollment.getStudent().getFirstName() + " is not active.");
        }
        Assessment assessment = assessmentMapper.toEntity(request);
        assessment.setEnrollment(enrollment);
        Assessment savedAssessment = assessmentRepository.save(assessment);
        return assessmentMapper.toDto(savedAssessment);
    }

    @Override
    @Transactional
    public AssessmentDto updateAssessment(Long assessmentId, UpdateAssessmentRequestDto request) {
        Assessment assessmentToUpdate = findAssessmentByIdOrThrow(assessmentId);
        assessmentMapper.updateFromDto(request, assessmentToUpdate);
        Assessment savedAssessment = assessmentRepository.save(assessmentToUpdate);
        return assessmentMapper.toDto(savedAssessment);
    }

    @Override
    @Transactional
    public void deleteAssessment(Long assessmentId) {
        Assessment assessmentToDelete = findAssessmentByIdOrThrow(assessmentId);
        assessmentRepository.delete(assessmentToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentDto> findAssessmentsByEnrollment(Long enrollmentId) {
        Enrollment enrollment = findEnrollmentByIdOrThrow(enrollmentId);
        List<Assessment> assessments = assessmentRepository.findAllByEnrollment(enrollment);
        return assessments.stream()
                .map(assessmentMapper::toDto)
                .toList();
    }

    private Assessment findAssessmentByIdOrThrow(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new AssessmentNotFoundException("Assessment not found with id: " + id));
    }

    private Enrollment findEnrollmentByIdOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException("Enrollment not found with id: " + id));
    }
}
