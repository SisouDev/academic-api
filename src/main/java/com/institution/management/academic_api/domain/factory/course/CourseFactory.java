package com.institution.management.academic_api.domain.factory.course;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.mapper.course.CourseMapper;
import com.institution.management.academic_api.domain.model.entities.course.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseFactory {

    private final CourseMapper courseMapper;

    public Course create(CreateCourseRequestDto requestDto) {
        return courseMapper.toEntity(requestDto);
    }
}