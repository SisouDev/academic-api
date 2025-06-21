package com.institution.management.academic_api.domain.factory.student;
import com.institution.management.academic_api.application.dto.student.CreateAssessmentRequestDto;
import com.institution.management.academic_api.application.mapper.simple.student.AssessmentMapper;
import com.institution.management.academic_api.domain.model.entities.student.Assessment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AssessmentFactory {

    private final AssessmentMapper assessmentMapper;

    public Assessment create(CreateAssessmentRequestDto requestDto) {
        Assessment assessment = assessmentMapper.toEntity(requestDto);
        assessment.setAssessmentDate(LocalDate.now());
        return assessment;
    }
}