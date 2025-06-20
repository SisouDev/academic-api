package com.institution.management.academic_api.domain.factory.course;
import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.mapper.course.SubjectMapper;
import com.institution.management.academic_api.domain.model.entities.course.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubjectFactory {

    private final SubjectMapper subjectMapper;

    public Subject create(CreateSubjectRequestDto requestDto) {
        return subjectMapper.toEntity(requestDto);
    }
}