package com.institution.management.academic_api.domain.service.course;

import com.institution.management.academic_api.application.dto.course.CourseSectionDetailsDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.course.CreateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.course.UpdateCourseSectionRequestDto;
import com.institution.management.academic_api.application.dto.teacher.CourseSectionDetailsForTeacherDto;

import java.util.List;

public interface CourseSectionService {
    CourseSectionDetailsDto create(CreateCourseSectionRequestDto request);
    CourseSectionDetailsDto findById(Long id);
    List<CourseSectionSummaryDto> findAllByAcademicTerm(Long academicTermId);
    CourseSectionDetailsDto update(Long id, UpdateCourseSectionRequestDto request);
    void delete(Long id);
    CourseSectionDetailsForTeacherDto findDetailsForTeacher(Long sectionId);
}
