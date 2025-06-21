package com.institution.management.academic_api.domain.factory.course;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.mapper.simple.course.CourseSectionMapper;
import com.institution.management.academic_api.domain.model.entities.course.CourseSection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseSectionFactory {

    private final CourseSectionMapper courseSectionMapper;

    public CourseSection create(CreateCourseSectionRequestDto requestDto) {
        return courseSectionMapper.toEntity(requestDto);
    }
}