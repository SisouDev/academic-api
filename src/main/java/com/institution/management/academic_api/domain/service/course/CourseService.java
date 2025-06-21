package com.institution.management.academic_api.domain.service.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseRequestDto;

import java.util.List;

public interface CourseService {
    CourseDetailsDto create(CreateCourseRequestDto request);

    CourseDetailsDto findById(Long id);

    List<CourseSummaryDto> findAllByDepartment(Long departmentId);

    CourseDetailsDto update(Long id, UpdateCourseRequestDto request);

    void delete(Long id);
}
