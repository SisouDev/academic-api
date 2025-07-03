package com.institution.management.academic_api.application.service.teacher;

import com.institution.management.academic_api.application.dto.teacher.GradebookDto;
import com.institution.management.academic_api.application.dto.teacher.GradebookGradeDto;
import com.institution.management.academic_api.application.dto.teacher.GradebookHeaderDto;
import com.institution.management.academic_api.application.dto.teacher.GradebookStudentRowDto;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import com.institution.management.academic_api.domain.model.entities.student.AssessmentDefinition;
import com.institution.management.academic_api.domain.model.entities.student.Enrollment;
import com.institution.management.academic_api.domain.repository.student.AssessmentDefinitionRepository;
import com.institution.management.academic_api.domain.repository.student.AssessmentRepository;
import com.institution.management.academic_api.domain.repository.student.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradebookService {

    private final EnrollmentRepository enrollmentRepository;
    private final AssessmentDefinitionRepository definitionRepository;
    private final AssessmentRepository assessmentRepository;

    @Transactional(readOnly = true)
    public GradebookDto getGradebookForSection(Long courseSectionId) {
        List<AssessmentDefinition> definitions = definitionRepository.findAllByCourseSectionId(courseSectionId);
        List<GradebookHeaderDto> headers = definitions.stream()
                .map(def -> new GradebookHeaderDto(def.getId(), def.getTitle()))
                .collect(Collectors.toList());

        List<Enrollment> enrollments = enrollmentRepository.findAllByCourseSectionId(courseSectionId);

        List<GradebookStudentRowDto> studentRows = enrollments.stream().map(enrollment -> {
            List<Assessment> studentAssessments = assessmentRepository.findAllByEnrollment(enrollment);

            Map<Long, GradebookGradeDto> gradesMap = studentAssessments.stream()
                    .collect(Collectors.toMap(
                            asm -> asm.getAssessmentDefinition().getId(),
                            asm -> new GradebookGradeDto(asm.getId(), asm.getScore())
                    ));

            return new GradebookStudentRowDto(
                    enrollment.getId(),
                    enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName(),
                    gradesMap
            );
        }).collect(Collectors.toList());

        return new GradebookDto(headers, studentRows);
    }
}