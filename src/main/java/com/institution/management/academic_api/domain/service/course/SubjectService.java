package com.institution.management.academic_api.domain.service.course;

import com.institution.management.academic_api.application.dto.course.CreateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.course.SubjectDetailsDto;
import com.institution.management.academic_api.application.dto.course.SubjectSummaryDto;
import com.institution.management.academic_api.application.dto.course.UpdateSubjectRequestDto;
import com.institution.management.academic_api.application.dto.student.StudentSubjectDetailsDto;

import java.util.List;

public interface SubjectService {
    SubjectDetailsDto create(CreateSubjectRequestDto request);

    SubjectDetailsDto findById(Long id);

    List<SubjectSummaryDto> findAllByCourse(Long courseId);

    SubjectDetailsDto update(Long id, UpdateSubjectRequestDto request);

    void delete(Long id);

    StudentSubjectDetailsDto findSubjectDetailsForPage(Long subjectId);

}
