package com.institution.management.academic_api.domain.service.teacher;

import com.institution.management.academic_api.application.dto.common.PersonSummaryDto;
import com.institution.management.academic_api.application.dto.course.CourseSectionSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.CreateTeacherRequestDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherResponseDto;
import com.institution.management.academic_api.application.dto.teacher.TeacherSummaryDto;
import com.institution.management.academic_api.application.dto.teacher.UpdateTeacherRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherService {
    TeacherResponseDto create(CreateTeacherRequestDto request);

    TeacherResponseDto findById(Long id);

    List<PersonSummaryDto> findAllByInstitution(Long institutionId);

    TeacherResponseDto update(Long id, UpdateTeacherRequestDto request);

    List<CourseSectionSummaryDto> findCourseSectionsByTeacherId(Long teacherId);

    Page<TeacherSummaryDto> findPaginated(String searchTerm, Pageable pageable);

}
