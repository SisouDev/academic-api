package com.institution.management.academic_api.domain.service.course;

import com.institution.management.academic_api.application.dto.course.CourseDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseDetailsDto create(CreateCourseRequestDto request);

    CourseDetailsDto findById(Long id);

    CourseDetailsDto update(Long id, UpdateCourseRequestDto request);

    Page<CourseSummaryDto> findPaginated(String searchTerm, Pageable pageable);

    void delete(Long id);
}
