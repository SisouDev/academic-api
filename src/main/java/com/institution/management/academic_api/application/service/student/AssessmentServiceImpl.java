package com.institution.management.academic_api.application.service.student;

import com.institution.management.academic_api.application.dto.student.AssessmentDto;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.dto.student.UpdateAssessmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.AssessmentMapper;
import com.institution.management.academic_api.application.notifiers.student.AssessmentNotifier;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import com.institution.management.academic_api.domain.repository.user.UserRepository;
import com.institution.management.academic_api.domain.service.common.NotificationService;
import com.institution.management.academic_api.domain.service.student.AssessmentService;
import com.institution.management.academic_api.exception.type.common.EntityNotFoundException;
import com.institution.management.academic_api.exception.type.student.AssessmentNotFoundException;
import com.institution.management.academic_api.exception.type.student.EnrollmentNotFoundException;
import com.institution.management.academic_api.infra.aplication.aop.LogActivity;
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
    private final AssessmentDefinitionRepository definitionRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final AssessmentNotifier assessmentNotifier;

    @Override
    @Transactional
    public AssessmentDto addAssessmentToEnrollment(CreateAssessmentRequestDto request) {
        Enrollment enrollment = findEnrollmentByIdOrThrow(request.enrollmentId());

        AssessmentDefinition definition = definitionRepository.findById(request.assessmentDefinitionId())
                .orElseThrow(() -> new EntityNotFoundException("Assessment Definition not found."));

        Assessment assessment = assessmentMapper.toEntity(request);

        assessment.setEnrollment(enrollment);
        assessment.setAssessmentDefinition(definition);

        if (assessment.getAssessmentDate() == null) {
            assessment.setAssessmentDate(definition.getAssessmentDate());
        }
        if (assessment.getType() == null) {
            assessment.setType(definition.getType());
        }

        Assessment savedAssessment = assessmentRepository.save(assessment);

        assessmentNotifier.notifyStudentOfNewGrade(savedAssessment);

        return assessmentMapper.toDto(savedAssessment);
    }

    @Override
    @Transactional
    @LogActivity("Atualizou uma avaliação.")
    public AssessmentDto updateAssessment(Long assessmentId, UpdateAssessmentRequestDto request) {
        Assessment assessmentToUpdate = findAssessmentByIdOrThrow(assessmentId);
        assessmentMapper.updateFromDto(request, assessmentToUpdate);
        Assessment savedAssessment = assessmentRepository.save(assessmentToUpdate);
        assessmentNotifier.notifyStudentOfGradeUpdate(assessmentToUpdate);
        return assessmentMapper.toDto(savedAssessment);
    }

    @Override
    @Transactional
    @LogActivity("Deletou uma avaliação.")
    public void deleteAssessment(Long assessmentId) {
        Assessment assessmentToDelete = findAssessmentByIdOrThrow(assessmentId);
        assessmentNotifier.notifyStudentOfGradeDeletion(assessmentToDelete);
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
